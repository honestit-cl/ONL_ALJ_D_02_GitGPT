package pl.coderslab.gitgpt.commit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import pl.coderslab.gitgpt.author.Author;
import pl.coderslab.gitgpt.repository.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChangeRepositoryTest {

  @Autowired TestEntityManager entityManager;

  @Autowired ChangeRepository repository;

  @Test
  //  Prawdopodobnie czytelniej byłoby przygotować skrypt sql dla tego testu lub
  //  zapytania sql dodać do adnotacji @Sql
  //  @Sql
  public void whenSearchForUncommittedChanges_thenFindOnlyFromGivenRepository() {
    Author author = Author.builder().name("author").build();
    entityManager.persist(author);
    Repository repository1 =
        Repository.builder().name("repository-1").url("url-1").owner(author).build();
    entityManager.persist(repository1);
    Change repo1Change1Uncommitted =
        Change.builder().path("path-1").repository(repository1).build();
    entityManager.persist(repo1Change1Uncommitted);
    Commit commit1 =
        Commit.builder().sha("sha-1").name("name-1").repository(repository1).author(author).build();
    entityManager.persist(commit1);
    Change repo1Change2Committed =
        Change.builder().path("path-2").repository(repository1).commit(commit1).build();
    entityManager.persist(repo1Change2Committed);
    Repository repository2 =
        Repository.builder().name("repository-2").url("url-2").owner(author).build();
    entityManager.persist(repository2);
    Change repo2Change1 = Change.builder().path("path-3").repository(repository2).build();
    entityManager.persist(repo2Change1);

    List<Change> changeList = repository.findAllByRepositoryAndCommitIsNull(repository1);

    assertThat(changeList).hasSize(1);
    assertThat(changeList.get(0)).isEqualTo(repo1Change1Uncommitted);
  }
}
