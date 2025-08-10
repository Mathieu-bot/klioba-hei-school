package school.hei.tsinjo.repository.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JEvent {
  @Id private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private JUser user;

  @ManyToOne
  @JoinColumn(name = "payment_id")
  private JPayment payment;

  private Instant creationInstant;
}
