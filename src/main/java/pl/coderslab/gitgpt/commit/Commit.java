package pl.coderslab.gitgpt.commit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commits")
@Getter
@Setter
@ToString
public class Commit {

  @Id @GeneratedValue private Long id;

  @Column(nullable = true, unique = true)
  private String sha;

  @Column(nullable = true)
  private String name;

  @Column(length = 1024)
  private String description;

  @Column(name = "created_on")
  private LocalDateTime createdOn;

  private boolean uploaded;
}
