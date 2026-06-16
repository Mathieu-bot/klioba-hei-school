package school.hei.tsinjo.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Help;
import school.hei.tsinjo.repository.jpa.JHelpRepository;
import school.hei.tsinjo.repository.mapper.JHelpMapper;

@Repository
@AllArgsConstructor
public class HelpRepository {

    private final JHelpRepository jHelpRepository;
    private final JHelpMapper jHelpMapper;

    public Help save(Help help) {
        return jHelpMapper.toDomain(
                jHelpRepository.save(jHelpMapper.toEntity(help))
        );
    }

    public Optional<Help> findById(String id) {
        return jHelpRepository.findById(id).map(jHelpMapper::toDomain);
    }

    public List<Help> findAll() {
        return jHelpRepository.findAll().stream()
                .map(jHelpMapper::toDomain)
                .toList();
    }
}