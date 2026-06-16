package school.hei.tsinjo.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.repository.jpa.model.JEvent;

@Component
@AllArgsConstructor
public class JDonationMapper {

    private final JPaymentMapper jPaymentMapper;
    private final JUserMapper jUserMapper;

    public Donation toDomain(JEvent jEvent) {
        return new Donation(
                jEvent.getId(),
                jPaymentMapper.toDomain(jEvent.getPayment()),
                jUserMapper.toDomain(jEvent.getUser()),
                jEvent.getCreationInstant()
        );
    }

    public JEvent toEntity(Donation donation) {
        return new JEvent(
                donation.getId(),
                jUserMapper.toEntity(donation.getUser()),
                jPaymentMapper.toEntity(donation.getPayment()),
                donation.getCreationInstant(),
                ""
        );
    }
}