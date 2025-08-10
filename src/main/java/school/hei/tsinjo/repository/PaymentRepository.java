package school.hei.tsinjo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.repository.jpa.JPaymentRepository;
import school.hei.tsinjo.repository.mapper.JPaymentMapper;

@Repository
@AllArgsConstructor
public class PaymentRepository {

  private final JPaymentRepository jPaymentRepository;
  private final JPaymentMapper jPaymentMapper;

  public Payment save(Payment payment) {
    var jPaymentToSave = jPaymentMapper.toEntity(payment);
    var jPaymentSaved = jPaymentRepository.save(jPaymentToSave);
    return jPaymentMapper.toDomain(jPaymentSaved);
  }
}
