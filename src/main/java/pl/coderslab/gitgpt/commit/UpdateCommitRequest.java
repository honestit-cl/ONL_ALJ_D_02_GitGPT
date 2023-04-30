package pl.coderslab.gitgpt.commit;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UpdateCommitRequest(
    @NotBlank String sha,
    @NotBlank String name,
    @Size(max = 1024) String description,
    @AssertTrue Boolean upload) {}
