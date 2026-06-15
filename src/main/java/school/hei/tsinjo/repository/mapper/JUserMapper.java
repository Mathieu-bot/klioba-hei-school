package school.hei.tsinjo.repository.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.repository.jpa.model.JUser;

@Component
@AllArgsConstructor
public class JUserMapper {

  private final JClubMapper jClubMapper;

  public User toDomain(JUser jUser) {
    var clubs = jUser.getClubs().stream().map(jClubMapper::toDomain).collect(Collectors.toList());

    return new User(
        jUser.getId(), jUser.getFirstName(), jUser.getLastName(), jUser.getEmail(), clubs);
  }

  public JUser toEntity(User user) {
    var jClubs = user.getClubs().stream().map(jClubMapper::toEntity).collect(Collectors.toList());

    var jUser =
        new JUser(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            new ArrayList<>());

    jUser.setClubs(jClubs);

    return jUser;
  }
}
