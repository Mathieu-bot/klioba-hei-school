package school.hei.tsinjo.model;

import java.time.Instant;

public final class Help extends Event {
  public Help(String id, Payment payment, User user, Instant date) {
    super(id, payment, user, date);
  }
}
