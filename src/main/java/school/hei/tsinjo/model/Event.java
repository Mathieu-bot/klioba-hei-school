package school.hei.tsinjo.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract sealed class Event permits Donation, Help {
  private final Payment payment;
  private final User user;
  private final LocalDate date;
}
