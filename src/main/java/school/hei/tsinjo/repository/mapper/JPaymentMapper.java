package school.hei.tsinjo.repository.mapper;

import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;
import static school.hei.tsinjo.model.PspType.ORANGE_MONEY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.repository.jpa.model.JPayment;

@Slf4j
@Component
public class JPaymentMapper {
  public Payment toDomain(JPayment jPayment) {
    log.warn("Manually set payment.pspType to ORANGE_MONEY: it could be wrong!");
    log.warn("Manually set payment.status to VERIFYING: it could be wrong!");
    return new Payment(jPayment.getAmount(), ORANGE_MONEY, VERIFYING); // TODO
  }
}
