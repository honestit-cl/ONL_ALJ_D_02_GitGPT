package pl.coderslab.gitgpt.commit;

import java.util.List;

public interface CommitManager {

  List<CommitSummary> getAll();
}
