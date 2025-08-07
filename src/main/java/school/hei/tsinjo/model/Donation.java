package school.hei.tsinjo.model;

import java.time.Instant;

public final class Donation extends Event {
  public Donation(Payment payment, User user, Instant date) {
    super(payment, user, date);
  }
}
