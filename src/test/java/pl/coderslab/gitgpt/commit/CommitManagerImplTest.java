package pl.coderslab.gitgpt.commit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.gitgpt.author.Author;
import pl.coderslab.gitgpt.author.AuthorRepository;
import pl.coderslab.gitgpt.repository.Repository;
import pl.coderslab.gitgpt.repository.RepositoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommitManagerImplTest {

  @Mock ChangeRepository changeRepository;
  @Mock CommitRepository commitRepository;
  @Mock AuthorRepository authorRepository;
  @Mock RepositoryRepository repositoryRepository;

  @InjectMocks CommitManagerImpl commitManager;

  @Test
  public void whenCreateCommit_withNonExistingAuthor_fail() {
    when(authorRepository.findByName("Non_exist")).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () ->
                commitManager.create(
                    new CreateCommitRequest(
                        "name", "description", "repositoryName", "main", "Non_exist")));
    assertThat(exception).hasMessageContaining("No author with name Non_exist");
  }

  @Test
  public void whenCreateCommit_withNonExistingRepository_fail() {
    Author author = Author.builder().name("author").build();
    when(authorRepository.findByName("author")).thenReturn(Optional.of(author));
    when(repositoryRepository.findByNameAndOwner("Non_exists", author))
        .thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () ->
                commitManager.create(
                    new CreateCommitRequest(
                        "name", "description", "Non_exists", "main", "author")));
    assertThat(exception)
        .hasMessageContaining("No repository with name Non_exists for author author");
  }

  @Test
  public void whenCreateCommit_success() {
    Author author = Author.builder().name("author").build();
    when(authorRepository.findByName("author")).thenReturn(Optional.of(author));
    Repository repository = Repository.builder().name("repository").owner(author).build();
    when(repositoryRepository.findByNameAndOwner("repository", author))
        .thenReturn(Optional.of(repository));
    List<Change> changeList =
        List.of(
            Change.builder().path("path-1").repository(repository).build(),
            Change.builder().path("path-2").repository(repository).build());
    when(changeRepository.findAllByRepositoryAndCommitIsNull(repository)).thenReturn(changeList);

    CommitSummary commitSummary =
        commitManager.create(
            new CreateCommitRequest("name", "description", "repository", "main", "author"));

    assertThat(commitSummary)
        .hasFieldOrPropertyWithValue("name", "name")
        .hasFieldOrPropertyWithValue("author", "author")
        .hasFieldOrPropertyWithValue("destination", "repository - main")
        .hasFieldOrPropertyWithValue("changeCount", 2);
  }
}
