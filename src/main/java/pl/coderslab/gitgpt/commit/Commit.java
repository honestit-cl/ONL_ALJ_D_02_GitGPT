package pl.coderslab.gitgpt.commit;

import lombok.*;
import pl.coderslab.gitgpt.author.Author;
import pl.coderslab.gitgpt.repository.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commits")
@Getter
@Setter
// Z generowanych metod typu toString, equals, hashCode wyrzucamy ("ekskludujemy") pola kolekcji i
// ogólnie z lazy loading. Patrz: @ToString.Exclude na polu changeList
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String sha;

  @Column(nullable = false)
  private String name;

  @Column(length = 1024)
  private String description;

  @Column(name = "created_on")
  private LocalDateTime createdOn;

  private boolean uploaded;

  private String branch;

  // Ta strona NIE jest właścicielem relacji
  // Domyślnie jest lazy loading czyli pobierając commit z bazy nie dostajemy od razu listy jego
  // zmian.
  @OneToMany(mappedBy = "commit")
  @ToString.Exclude
  private List<Change> changeList = new ArrayList<>();

  @ManyToOne(optional = false)
  private Repository repository;

  @ManyToOne(optional = false)
  private Author author;

  // Wywoływane przez Hibernate przed zapisem encji, ustawi nam datę utworzenia automatycznie i nie
  // musimy się nią przejmować.
  @PrePersist
  public void prePersist() {
    this.createdOn = LocalDateTime.now();
  }
}
