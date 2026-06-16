package school.hei.tsinjo.model;

import java.time.Instant;

public final class Help extends Event {
  public Help(String id, Payment payment, User user, Instant creationInstant, String comment) {
    super(id, payment, user, creationInstant, comment);
  }

  @Override
  public Event withPayment(Payment newPayment) {
    return new Help(id, newPayment, user, creationInstant, comment);
  }
}
