package school.hei.tsinjo.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Donor;
import school.hei.tsinjo.repository.jpa.JDonorRepository;
import school.hei.tsinjo.repository.mapper.JDonorMapper;

@Repository
@AllArgsConstructor
public class DonorRepository {

    private final JDonorRepository jDonorRepository;
    private final JDonorMapper jDonorMapper;

    public Donor save(Donor donor) {
        return jDonorMapper.toDomain(
                jDonorRepository.save(jDonorMapper.toEntity(donor))
        );
    }

    public Optional<Donor> findById(String id) {
        return jDonorRepository.findById(id).map(jDonorMapper::toDomain);
    }

    public List<Donor> findAll() {
        return jDonorRepository.findAll().stream()
                .map(jDonorMapper::toDomain)
                .toList();
    }
}