package pl.coderslab.gitgpt.commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/commits")
@Slf4j
@RequiredArgsConstructor
public class CommitController {

  private final CommitManager commitManager;

  @GetMapping
  public List<CommitSummary> getAllCommits() {
    List<CommitSummary> commits = commitManager.getAll();
    log.debug("Collected {} commits", commits.size());
    return commits;
  }
}
