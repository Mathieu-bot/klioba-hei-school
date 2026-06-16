package school.hei.klioba.repository;

import static java.util.UUID.randomUUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.klioba.model.User;
import school.hei.klioba.repository.jpa.JUserRepository;
import school.hei.klioba.repository.jpa.model.JUser;
import school.hei.klioba.repository.mapper.JUserMapper;

import java.util.List;

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
        jUserRepository.save(new JUser(randomUUID().toString(), email, firstName, lastName, List.of())));
  }

  public User save(User user) {
    return jUserMapper.toDomain(jUserRepository.save(jUserMapper.toEntity(user)));
  }
}
