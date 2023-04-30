package pl.coderslab.gitgpt.commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommitManagerImpl implements CommitManager {

  private final CommitRepository commitRepository;

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

  private CommitSummary toSummary(Commit commit) {
    return new CommitSummary(
        commit.getSha(),
        commit.getAuthor().getName(),
        commit.getName(),
        commit.getRepository().getName() + " - " + commit.getBranch(),
        commit.getCreatedOn());
  }
}
