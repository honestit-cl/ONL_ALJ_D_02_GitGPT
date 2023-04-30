package pl.coderslab.gitgpt.commit;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, Long> {
  Optional<Commit> findBySha(String sha);

  // Instruujemy hibernate jakie relacje/atrybuty naszej encji powinny być załadowane w
  // zapytaniu/dostępne w wynikach
  @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "changeList")
  List<Commit> findAllWithChangeListBy();
}
