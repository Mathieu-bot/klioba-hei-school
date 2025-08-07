package school.hei.tsinjo.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.Help;
import school.hei.tsinjo.repository.jpa.model.JEvent;

@Component
@AllArgsConstructor
public class JEventMapper {

  private final JPaymentMapper jPaymentMapper;
  private final JUserMapper jUserMapper;

  public Event toDomain(JEvent jEvent) {
    var payment = jPaymentMapper.toDomain(jEvent.getPayment());
    var user = jUserMapper.toDomain(jEvent.getUser());
    var creationInstant = jEvent.getPayment().getCreationInstant();
    return payment.amount() >= 0
        ? new Donation(payment, user, creationInstant)
        : new Help(payment, user, creationInstant);
  }
}
