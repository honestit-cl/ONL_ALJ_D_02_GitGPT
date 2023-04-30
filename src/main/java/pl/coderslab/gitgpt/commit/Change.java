package pl.coderslab.gitgpt.commit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "changes")
@Getter
@Setter
@ToString
public class Change {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String path;
  // Kolumna w bazie będzie posiadała wartości zgodne z nazwami enuma.
  @Enumerated(EnumType.STRING)
  private ChangeType type;

  //  Ta strona jest właścicielem relacji. Chcąc powiązać commit ze zmianą, trzeba ustawić wartość
  // pola commit wewnątrz obiektu Change.
  @ManyToOne private Commit commit;
}
