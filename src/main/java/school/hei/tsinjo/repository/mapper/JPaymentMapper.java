package school.hei.tsinjo.repository.mapper;

import static school.hei.tsinjo.model.PspType.ORANGE_MONEY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PspType;
import school.hei.tsinjo.repository.jpa.model.JPayment;

@Slf4j
@Component
public class JPaymentMapper {
  public Payment toDomain(JPayment jPayment) {
    return switch (PspType.values()[0]) {
      case ORANGE_MONEY ->
          new Payment(
              jPayment.getId(),
              jPayment.getAmount(),
              ORANGE_MONEY,
              jPayment.getPspId(),
              jPayment.getStatus(),
              jPayment.getPspLastVerificationInstant());
    };
  }

  public JPayment toEntity(Payment payment) {
    return new JPayment(
        payment.id(),
        payment.amount(),
        payment.status(),
        payment.pspId(),
        payment.pspLastVerificationInstant());
  }
}
