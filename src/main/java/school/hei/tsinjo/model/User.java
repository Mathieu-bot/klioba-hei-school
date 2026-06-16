package school.hei.tsinjo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public sealed class User permits Member, Beneficiary {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
}
