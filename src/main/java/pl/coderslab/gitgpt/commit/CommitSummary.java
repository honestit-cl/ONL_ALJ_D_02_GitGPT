package pl.coderslab.gitgpt.commit;

import java.time.LocalDateTime;

public record CommitSummary(
    String sha, String author, String name, String destination, LocalDateTime date) {}
