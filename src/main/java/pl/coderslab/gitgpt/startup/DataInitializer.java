package pl.coderslab.gitgpt.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.gitgpt.author.Author;
import pl.coderslab.gitgpt.author.AuthorRepository;
import pl.coderslab.gitgpt.commit.*;
import pl.coderslab.gitgpt.repository.Repository;
import pl.coderslab.gitgpt.repository.RepositoryRepository;

@Component
// Ten komponent jest aktywny tylko wtedy, gdy wybranym profile jest profil "local"
@Profile("local")
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

  private final AuthorRepository authorRepository;
  private final RepositoryRepository repositoryRepository;
  private final CommitRepository commitRepository;
  private final ChangeRepository changeRepository;

  @EventListener
  @Transactional
  public void loadInitialData(ContextRefreshedEvent unused) {
    Author iluvatar = Author.builder().name("iluvatar").build();
    authorRepository.save(iluvatar);

    Repository designPatternsRepo =
        Repository.builder()
            .name("design-patterns")
            .owner(iluvatar)
            .url("https://github.com/iluvatar/design-patterns")
            .isPublic(true)
            .build();
    repositoryRepository.save(designPatternsRepo);

    Change change1 =
        Change.builder()
            .path("src/main/java/pl/coderslab/gitgpt/GitGptApplication")
            .type(ChangeType.ADDITION)
            .repository(designPatternsRepo)
            .build();
    changeRepository.save(change1);

    Change change2 =
        Change.builder()
            .path("src/test/http/cammits.http")
            .type(ChangeType.DELETION)
            .repository(designPatternsRepo)
            .build();
    changeRepository.save(change2);

    Change change3 =
        Change.builder()
            .path("README.md")
            .type(ChangeType.MODIFICATION)
            .repository(designPatternsRepo)
            .build();
    changeRepository.save(change3);

    Commit commit1 =
        Commit.builder()
            .name("Add Factory implementation")
            .branch("main")
            .repository(designPatternsRepo)
            .author(iluvatar)
            .sha("02af2acb-fa0b-4074-99d8-7e104ed2096d")
            .build();
    commitRepository.save(commit1);

    change1.setCommit(commit1);
    changeRepository.save(change1);

    change2.setCommit(commit1);
    changeRepository.save(change2);

    Commit commit2 =
        Commit.builder()
            .name("Add brief description to README.md")
            .branch("main")
            .repository(designPatternsRepo)
            .author(iluvatar)
            .sha("02af2acb-cb07-4074-99d8-7e104ed2096d")
            .build();
    commitRepository.save(commit2);

    change3.setCommit(commit2);
    changeRepository.save(change3);

    Change uncommittedChange1 =
        Change.builder()
            .repository(designPatternsRepo)
            .type(ChangeType.MODIFICATION)
            .path("src/test/commits.http")
            .build();
    changeRepository.save(uncommittedChange1);

    Change uncommittedChange2 =
        Change.builder()
            .repository(designPatternsRepo)
            .type(ChangeType.DELETION)
            .path("data/test.txt")
            .build();
    changeRepository.save(uncommittedChange2);
  }
}
