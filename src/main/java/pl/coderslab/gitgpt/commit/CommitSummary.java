package pl.coderslab.gitgpt.commit;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CommitSummary(
    String sha,
    String author,
    String name,
    String destination,
    @JsonFormat(pattern = "eee MMM dd hh:mm:ss yyyy") LocalDateTime date,
    Integer changeCount) {}
