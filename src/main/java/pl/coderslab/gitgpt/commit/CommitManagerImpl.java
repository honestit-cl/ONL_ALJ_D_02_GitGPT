package pl.coderslab.gitgpt.commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.gitgpt.author.Author;
import pl.coderslab.gitgpt.author.AuthorRepository;
import pl.coderslab.gitgpt.repository.Repository;
import pl.coderslab.gitgpt.repository.RepositoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommitManagerImpl implements CommitManager {

  private final CommitRepository commitRepository;
  private final ChangeRepository changeRepository;
  private final AuthorRepository authorRepository;
  private final RepositoryRepository repositoryRepository;

  @Override
  public List<CommitSummary> getAll() {
    List<Commit> commits = commitRepository.findAll();
    return commits.stream().map(this::toSummary).collect(Collectors.toList());
  }

  @Override
  public Optional<CommitSummary> getBySha(String sha) {
    Optional<Commit> commit = commitRepository.findBySha(sha);
    return commit.map(this::toSummary);
  }

  @Override
  public CommitSummary update(UpdateCommitRequest request) {
    return commitRepository
        .findBySha(request.sha())
        .map(
            commit -> {
              commit.setName(request.name());
              if (request.description() != null) {
                commit.setDescription(request.description());
              }
              if (request.upload() != null) {
                if (commit.isUploaded()) {
                  throw new IllegalStateException("Commit already uploaded");
                }
                commit.setUploaded(true);
              }
              return commit;
            })
        .map(commitRepository::save)
        .map(this::toSummary)
        .orElseThrow(() -> new IllegalArgumentException("No commit with sha " + request.sha()));
  }

  @Override
  public CommitSummary create(CreateCommitRequest request) {
    Author author =
        authorRepository
            .findByName(request.authorName())
            .orElseThrow(
                () -> new IllegalArgumentException("No author with name " + request.authorName()));
    Repository repository =
        repositoryRepository
            .findByNameAndOwner(request.repositoryName(), author)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No repository with name "
                            + request.repositoryName()
                            + " for author "
                            + author.getName()));
    List<Change> changeList = changeRepository.findAllByRepositoryAndCommitIsNull(repository);

    if (changeList.isEmpty()) {
      throw new IllegalStateException("No changes");
    }

    Commit commit =
        Commit.builder()
            .sha(UUID.randomUUID().toString())
            .author(author)
            .repository(repository)
            .branch(request.branch() != null ? request.branch() : "main")
            .name(request.name())
            .description(request.description())
            .uploaded(false)
            .changeList(changeList)
            .build();
    commitRepository.save(commit);

    for (Change change : changeList) {
      change.setCommit(commit);
    }

    changeRepository.saveAll(changeList);
    return toSummary(commit);
  }

  private CommitSummary toSummary(Commit commit) {
    return new CommitSummary(
        commit.getSha(),
        commit.getAuthor().getName(),
        commit.getName(),
        commit.getRepository().getName() + " - " + commit.getBranch(),
        commit.getCreatedOn());
  }
}
