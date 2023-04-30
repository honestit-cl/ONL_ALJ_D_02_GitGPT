package pl.coderslab.gitgpt.commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommitManagerImpl implements CommitManager {

  @Override
  public List<CommitSummary> getAll() {
    return List.of();
  }
}
