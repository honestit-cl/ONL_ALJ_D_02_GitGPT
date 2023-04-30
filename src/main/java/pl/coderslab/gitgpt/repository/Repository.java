package pl.coderslab.gitgpt.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.coderslab.gitgpt.author.Author;

import javax.persistence.*;
import java.util.List;

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

  @ManyToOne(optional = false)
  private Author owner;

  @ManyToMany
  // Ustalamy wprost nazwę tabeli łączącej w relacji wiele-do-wielu oraz nazwy kolumn z kluczami
  // obcymi (joinColumns dla relacji do encji, w której "jesteśmy", a inverseJoinColumns do relacji
  // do encji, na którą "wskazujemy").
  @JoinTable(
      name = "collaborators",
      joinColumns = @JoinColumn(name = "repository_id"),
      inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
  @ToString.Exclude
  private List<Author> collaborators;
}
