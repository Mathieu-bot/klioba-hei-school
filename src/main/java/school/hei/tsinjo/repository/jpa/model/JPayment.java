package school.hei.tsinjo.repository.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JPayment {
  @Id private String id;

  private int amount;

  private Instant creationInstant;
}
