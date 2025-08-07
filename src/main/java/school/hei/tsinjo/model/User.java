package school.hei.tsinjo.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public sealed class User permits Donor, Beneficiary {
  private final String firstName;
  private final String lastName;
  private final String email;
}
