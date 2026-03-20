package school.hei.tsinjo.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public abstract sealed class Event permits Donation, Help {
  protected final String id;
  protected final Payment payment;
  protected final User user;
  protected final Instant creationInstant;
  protected final String comment;

  public static Event from(
      String id, Payment payment, User user, Instant creationInstant, String comment) {
    return payment.amount() == null || payment.amount() >= 0
        ? new Donation(id, payment, user, creationInstant)
        : new Help(id, payment, user, creationInstant, comment);
  }

  public abstract Event withPayment(Payment newPayment);
}
