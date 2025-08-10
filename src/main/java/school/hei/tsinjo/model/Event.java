package school.hei.tsinjo.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public abstract sealed class Event permits Donation, Help {
  private final String id;
  private final Payment payment;
  private final User user;
  private final Instant creationInstant;

  public static Event from(String id, Payment payment, User user, Instant creationInstant) {
    return payment.amount() == null || payment.amount() >= 0
        ? new Donation(id, payment, user, creationInstant)
        : new Help(id, payment, user, creationInstant);
  }
}
