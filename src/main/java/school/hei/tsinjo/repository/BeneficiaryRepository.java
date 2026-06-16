package school.hei.tsinjo.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Beneficiary;
import school.hei.tsinjo.repository.jpa.JBeneficiaryRepository;
import school.hei.tsinjo.repository.mapper.JBeneficiaryMapper;

@Repository
@AllArgsConstructor
public class BeneficiaryRepository {

    private final JBeneficiaryRepository jBeneficiaryRepository;
    private final JBeneficiaryMapper jBeneficiaryMapper;

    public Beneficiary save(Beneficiary beneficiary) {
        return jBeneficiaryMapper.toDomain(
                jBeneficiaryRepository.save(jBeneficiaryMapper.toEntity(beneficiary))
        );
    }

    public Optional<Beneficiary> findById(String id) {
        return jBeneficiaryRepository.findById(id).map(jBeneficiaryMapper::toDomain);
    }

    public List<Beneficiary> findAll() {
        return jBeneficiaryRepository.findAll().stream()
                .map(jBeneficiaryMapper::toDomain)
                .toList();
    }
}