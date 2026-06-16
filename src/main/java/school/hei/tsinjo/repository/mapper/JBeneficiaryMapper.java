package school.hei.tsinjo.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Beneficiary;
import school.hei.tsinjo.repository.jpa.model.JUser;

@Component
public class JBeneficiaryMapper {

    public Beneficiary toDomain(JUser jUser) {
        return new Beneficiary(
                jUser.getId(),
                jUser.getFirstName(),
                jUser.getLastName(),
                jUser.getEmail()
        );
    }

    public JUser toEntity(Beneficiary beneficiary) {
        return new JUser(
                beneficiary.getId(),
                beneficiary.getEmail(),
                beneficiary.getFirstName(),
                beneficiary.getLastName()
        );
    }
}