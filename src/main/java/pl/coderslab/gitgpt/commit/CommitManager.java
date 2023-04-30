package pl.coderslab.gitgpt.commit;

import java.util.List;
import java.util.Optional;

public interface CommitManager {

  List<CommitSummary> getAll();

  Optional<CommitSummary> getBySha(String sha);

  CommitSummary update(UpdateCommitRequest request);
}
