package school.hei.tsinjo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public sealed class User permits Donor, Beneficiary {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
}
