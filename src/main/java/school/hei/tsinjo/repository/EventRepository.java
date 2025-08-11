package school.hei.tsinjo.repository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.repository.jpa.JEventRepository;
import school.hei.tsinjo.repository.mapper.JEventMapper;

@Repository
@AllArgsConstructor
public class EventRepository {

  private final JEventRepository jEventRepository;
  private final JEventMapper jEventMapper;

  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;

  public Event save(Event event) {
    paymentRepository.save(event.getPayment());
    userRepository.save(event.getUser());
    return jEventMapper.toDomain(jEventRepository.save(jEventMapper.toEntity(event)));
  }

  public List<Event> findAllByOrderByCreationInstantDesc() {
    return jEventRepository.findAllByOrderByCreationInstantDesc().stream()
        .map(jEventMapper::toDomain)
        .toList();
  }
}
