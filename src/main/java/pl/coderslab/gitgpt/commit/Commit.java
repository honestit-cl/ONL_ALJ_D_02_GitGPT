package pl.coderslab.gitgpt.commit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commits")
@Getter
@Setter
// Z generowanych metod typu toString, equals, hashCode wyrzucamy ("ekskludujemy") pola kolekcji i
// ogólnie z lazy loading.
@ToString(exclude = "changeList")
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

  // Ta strona NIE jest właścicielem relacji
  // Domyślnie jest lazy loading czyli pobierając commit z bazy nie dostajemy od razu listy jego
  // zmian.
  @OneToMany(mappedBy = "commit")
  @ToString.Exclude
  private List<Change> changeList = new ArrayList<>();
}
