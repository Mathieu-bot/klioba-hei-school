package school.hei.tsinjo.repository.mapper;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Club;
import school.hei.tsinjo.repository.jpa.model.JClub;

@Component
public class JClubMapper {

  public Club toDomain(JClub jClub) {
    return new Club(jClub.getId(), jClub.getName());
  }

  public JClub toEntity(Club club) {
    return new JClub(club.getId(), club.getName(), new ArrayList<>());
  }
}
