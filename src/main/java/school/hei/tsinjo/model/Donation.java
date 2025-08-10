package school.hei.tsinjo.model;

import java.time.Instant;

public final class Donation extends Event {
  public Donation(String id, Payment payment, User user, Instant date) {
    super(id, payment, user, date);
  }
}
