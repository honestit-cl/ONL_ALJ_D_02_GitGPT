package pl.coderslab.gitgpt.commit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateCommitRequest(
    @NotBlank String name,
    @Size(max = 1024) String description,
    @NotBlank String repositoryName,
    String branch,
    @NotBlank String authorName) {}
