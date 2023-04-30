package pl.coderslab.gitgpt.commit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, Long> {
  Optional<Commit> findBySha(String sha);
}
