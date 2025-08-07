package school.hei.tsinjo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.repository.jpa.model.JUser;

@Component
public class JUserMapper {
  public User toDomain(JUser jUser) {
    return new User(jUser.getFirstName(), jUser.getLastName(), jUser.getEmail());
  }
}
