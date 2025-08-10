package school.hei.tsinjo.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.repository.jpa.model.JEvent;

@Component
@AllArgsConstructor
public class JEventMapper {

  private final JPaymentMapper jPaymentMapper;
  private final JUserMapper jUserMapper;

  public Event toDomain(JEvent jEvent) {
    var payment = jPaymentMapper.toDomain(jEvent.getPayment());
    var user = jUserMapper.toDomain(jEvent.getUser());
    var creationInstant = jEvent.getCreationInstant();
    return Event.from(jEvent.getId(), payment, user, creationInstant);
  }

  public JEvent toEntity(Event event) {
    var jUser = jUserMapper.toEntity(event.getUser());
    var jPayment = jPaymentMapper.toEntity(event.getPayment());
    return new JEvent(event.getId(), jUser, jPayment, event.getCreationInstant());
  }
}
