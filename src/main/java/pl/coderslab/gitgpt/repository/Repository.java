package pl.coderslab.gitgpt.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@ToString
public class Repository {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String url;

  private boolean isPublic;
  private boolean isFork;
}
