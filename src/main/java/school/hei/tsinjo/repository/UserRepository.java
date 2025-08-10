package school.hei.tsinjo.repository;

import static java.util.UUID.randomUUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.repository.jpa.JUserRepository;
import school.hei.tsinjo.repository.jpa.model.JUser;
import school.hei.tsinjo.repository.mapper.JUserMapper;

@Repository
@AllArgsConstructor
public class UserRepository {

  private final JUserRepository jUserRepository;
  private final JUserMapper jUserMapper;

  public User saveIfEmailNotExist(String firstName, String lastName, String email) {
    var userOpt = jUserRepository.findByEmail(email);
    if (userOpt.isPresent()) {
      return userOpt.map(jUserMapper::toDomain).get();
    }

    return jUserMapper.toDomain(
        jUserRepository.save(new JUser(randomUUID().toString(), email, firstName, lastName)));
  }
}
