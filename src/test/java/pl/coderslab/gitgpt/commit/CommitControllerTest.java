package pl.coderslab.gitgpt.commit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommitController.class)
class CommitControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean CommitManager commitManager;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void whenCreateCommit_success() throws Exception {
    CreateCommitRequest request =
        new CreateCommitRequest("commit", "description", "repositoryName", "branch", "authorName");
    CommitSummary summary =
        new CommitSummary(
            "sha", "authorName", "commit", "repositoryName - branch", LocalDateTime.now(), 1);
    when(commitManager.create(request)).thenReturn(summary);

    mockMvc
        .perform(
            post("/api/commits")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string(LOCATION, "/api/commits/sha"));
  }

  @Test
  public void whenCreateCommit_withMissingName_fail() throws Exception {
    CreateCommitRequest request =
        new CreateCommitRequest(null, "description", "repositoryName", "branch", "authorName");

    mockMvc
        .perform(
            post("/api/commits")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
    // W teście jednostkowym izolowanym do naszego pojedynczego kontrolera nie działa mechanizm
    // serializacji
    // błędów walidacji do json'a.
    //        .andExpect(jsonPath("$.errors[0].field", equalTo("name")))
    //        .andExpect(jsonPath("$.errors[0].defaultMessage", equalTo("nie może być odstępem")));
  }
}
