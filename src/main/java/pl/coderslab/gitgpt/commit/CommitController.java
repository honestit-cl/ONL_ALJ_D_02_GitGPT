package pl.coderslab.gitgpt.commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

  @GetMapping("/{sha}")
  public ResponseEntity<CommitSummary> getCommit(@PathVariable String sha) {
    Optional<CommitSummary> commitSummary = commitManager.getBySha(sha);
    return commitSummary.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{sha}")
  public ResponseEntity<CommitSummary> updateCommit(
      @PathVariable String sha, @RequestBody @Valid UpdateCommitRequest request) {
    if (!sha.equals(request.sha())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sha mismatch");
    }
    CommitSummary summary = commitManager.update(request);
    return ResponseEntity.ok(summary);
  }

  @PostMapping
  public ResponseEntity<?> createCommit(@RequestBody @Valid CreateCommitRequest request) {
    CommitSummary summary = commitManager.create(request);
    return ResponseEntity.created(URI.create("/api/commits/" + summary.sha())).build();
  }
}
