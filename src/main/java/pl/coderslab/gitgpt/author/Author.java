package pl.coderslab.gitgpt.author;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.coderslab.gitgpt.repository.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@ToString
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String firstName;
  private String lastName;

  @OneToMany(mappedBy = "owner")
  @ToString.Exclude
  private List<Repository> repositories = new ArrayList<>();
}
