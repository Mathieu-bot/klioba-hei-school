package school.hei.tsinjo.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public sealed class User permits Member, WithDrawer {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final List<Club> clubs;
}
