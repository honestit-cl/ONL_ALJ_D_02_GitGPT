package pl.coderslab.gitgpt.commit;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.gitgpt.repository.Repository;

import java.util.List;

public interface ChangeRepository extends JpaRepository<Change, Long> {

  List<Change> findAllByRepositoryAndCommitIsNull(Repository repository);
}
