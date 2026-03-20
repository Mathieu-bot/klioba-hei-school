package school.hei.tsinjo.model;

import java.time.Instant;

public final class Donation extends Event {
  public Donation(String id, Payment payment, User user, Instant creationInstant) {
    super(id, payment, user, creationInstant, "");
  }

  @Override
  public Event withPayment(Payment newPayment) {
    return new Donation(id, newPayment, user, creationInstant);
  }
}
