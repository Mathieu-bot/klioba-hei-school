package school.hei.tsinjo.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract sealed class Event permits Donation, Help {
  private final Payment payment;
  private final User user;
  private final Instant date;
}
