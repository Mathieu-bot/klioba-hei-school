package school.hei.tsinjo.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.repository.jpa.JDonationRepository;
import school.hei.tsinjo.repository.mapper.JDonationMapper;

@Repository
@AllArgsConstructor
public class DonationRepository {

    private final JDonationRepository jDonationRepository;
    private final JDonationMapper jDonationMapper;

    public Donation save(Donation donation) {
        return jDonationMapper.toDomain(
                jDonationRepository.save(jDonationMapper.toEntity(donation))
        );
    }

    public Optional<Donation> findById(String id) {
        return jDonationRepository.findById(id).map(jDonationMapper::toDomain);
    }

    public List<Donation> findAll() {
        return jDonationRepository.findAll().stream()
                .map(jDonationMapper::toDomain)
                .toList();
    }
}