package school.hei.tsinjo.model;

import java.time.Instant;

public final class Help extends Event {
  public Help(Payment payment, User user, Instant date) {
    super(payment, user, date);
  }
}
