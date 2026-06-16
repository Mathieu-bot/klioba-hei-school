package school.hei.klioba.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.klioba.model.User;
import school.hei.klioba.repository.jpa.model.JUser;

@Component
public class JUserMapper {
  public User toDomain(JUser jUser) {
    return new User(jUser.getId(), jUser.getFirstName(), jUser.getLastName(), jUser.getEmail());
  }

  public JUser toEntity(User user) {
    return new JUser(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
  }
}
