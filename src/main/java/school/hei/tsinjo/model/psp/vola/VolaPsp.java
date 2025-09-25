package school.hei.tsinjo.model.psp.vola;

import static school.hei.tsinjo.model.PaymentStatus.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.psp.Psp;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.api.VolaClient;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.PspPayment;

@AllArgsConstructor
public class VolaPsp implements Psp {
  private final VolaClient volaClient;

  @Override
  public Payment create(String tsinjoId, PspType pspType, String pspId, String email) {
    var volaPayment = volaClient.create(pspType, pspId, email);
    return toPayment(tsinjoId, volaPayment);
  }

  @Override
  public Payment get(String tsinjoId, PspType pspType, String pspId, String email) {
    var volaPayment = volaClient.get(pspType, pspId, email);
    return toPayment(tsinjoId, volaPayment);
  }

  private Payment toPayment(
      String tsinjoId, school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment volaPayment) {

    var volaPspPayment = volaPayment.getPspPayment();

    // ✅ gestion du null pour éviter le NPE
    Instant lastVerificationInstant =
        Optional.ofNullable(volaPayment.getLastPspVerificationInstant())
            .map(Date::toInstant)
            .orElse(null);

    return new Payment(
        tsinjoId,
        volaPspPayment.getAmount(),
        toPspType(volaPspPayment.getPspType()),
        volaPspPayment.getId(),
        toPaymentStatus(volaPayment.getVerificationStatus()),
        lastVerificationInstant,
        volaPayment.getCreationInstant());
  }

  private PspType toPspType(PspPayment.PspTypeEnum volaPspType) {
    return switch (volaPspType) {
      case ORANGE_MONEY -> PspType.ORANGE_MONEY;
    };
  }

  private PaymentStatus toPaymentStatus(
      school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum
          volaPaymentStatus) {
    return switch (volaPaymentStatus) {
      case VERIFYING -> VERIFYING;
      case SUCCEEDED -> CONFIRMED;
      case FAILED -> REFUSED;
    };
  }
}
