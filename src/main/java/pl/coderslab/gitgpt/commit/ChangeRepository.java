package pl.coderslab.gitgpt.commit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeRepository extends JpaRepository<Change, Long> {}
