package school.hei.tsinjo.repository.jpa.model;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import school.hei.tsinjo.model.PaymentStatus;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JPayment {
  @Id private String id;
  private Integer amount;

  @Enumerated(STRING)
  private PaymentStatus status;

  private String pspId;
  private Instant pspLastVerificationInstant;
  private Instant creationInstant;
}
