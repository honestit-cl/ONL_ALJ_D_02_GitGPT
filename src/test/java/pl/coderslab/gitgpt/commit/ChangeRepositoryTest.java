package pl.coderslab.gitgpt.commit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import pl.coderslab.gitgpt.repository.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// Wykorzystanie skryptu ChangeRepositoryTest.sql do przygotowania danych dla testu
@Sql
class ChangeRepositoryTest {

  @Autowired TestEntityManager entityManager;

  @Autowired ChangeRepository repository;

  @Test
  public void whenSearchForUncommittedChanges_thenFindOnlyFromGivenRepository() {
    Repository repository = entityManager.find(Repository.class, 1L);

    List<Change> changeList = this.repository.findAllByRepositoryAndCommitIsNull(repository);

    assertThat(changeList).hasSize(1);
    assertThat(changeList.get(0)).isNotNull();
    assertThat(changeList.get(0)).hasFieldOrPropertyWithValue("id", 2L);
  }
}
