package school.hei.tsinjo.model;

import java.time.LocalDate;

public final class Donation extends Event {
  public Donation(Payment payment, User user, LocalDate date) {
    super(payment, user, date);
  }
}
