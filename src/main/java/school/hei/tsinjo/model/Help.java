package school.hei.tsinjo.model;

import java.time.LocalDate;

public final class Help extends Event {
  public Help(Payment payment, User user, LocalDate date) {
    super(payment, user, date);
  }
}
