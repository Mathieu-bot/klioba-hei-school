package school.hei.tsinjo.model;

import java.time.Instant;

public final class Help extends Event {
  public Help(String id, Payment payment, User user, Instant creationInstant) {
    super(id, payment, user, creationInstant);
  }

  @Override
  public Event withPayment(Payment newPayment) {
    return new Help(id, newPayment, user, creationInstant);
  }
}
