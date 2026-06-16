package school.hei.tsinjo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Donor;
import school.hei.tsinjo.repository.jpa.model.JUser;

@Component
public class JDonorMapper {

    public Donor toDomain(JUser jUser) {
        return new Donor(
                jUser.getId(),
                jUser.getFirstName(),
                jUser.getLastName(),
                jUser.getEmail()
        );
    }

    public JUser toEntity(Donor donor) {
        return new JUser(
                donor.getId(),
                donor.getEmail(),
                donor.getFirstName(),
                donor.getLastName()
        );
    }
}