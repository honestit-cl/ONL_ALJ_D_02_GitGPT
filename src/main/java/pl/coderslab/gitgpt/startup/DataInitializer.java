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

import java.util.UUID;

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
            .build();
    changeRepository.save(change1);

    Change change2 =
        Change.builder().path("src/test/http/cammits.http").type(ChangeType.DELETION).build();

    changeRepository.save(change2);
    Commit commit1 =
        Commit.builder()
            .name("Add Factory implementation")
            .branch("main")
            .repository(designPatternsRepo)
            .author(iluvatar)
            .sha(UUID.randomUUID().toString())
            .build();
    commitRepository.save(commit1);

    change1.setCommit(commit1);
    changeRepository.save(change1);

    change2.setCommit(commit1);
    changeRepository.save(change2);
  }
}
