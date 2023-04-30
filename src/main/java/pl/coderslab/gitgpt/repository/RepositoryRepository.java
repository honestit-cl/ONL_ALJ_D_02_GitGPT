package pl.coderslab.gitgpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.gitgpt.author.Author;

import java.util.Optional;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
  Optional<Repository> findByNameAndOwner(String name, Author owner);
}
