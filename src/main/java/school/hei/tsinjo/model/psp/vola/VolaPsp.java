package school.hei.tsinjo.model.psp.vola;

import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.psp.Psp;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.api.VolaClient;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.PspPayment;

@Slf4j
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
    if (tsinjoId == null) {
      throw new IllegalArgumentException("tsinjoId cannot be null");
    }
    if (volaPayment == null) {
      throw new IllegalArgumentException("Vola payment is null for tsinjoId: " + tsinjoId);
    }

    var volaPspPayment = volaPayment.getPspPayment();

    var lastVerificationInstant =
        volaPayment.getLastPspVerificationInstant() != null
            ? volaPayment.getLastPspVerificationInstant().toInstant()
            : null;

    var creationInstant =
        volaPspPayment == null || volaPspPayment.getCreationInstant() == null
            ? null
            : volaPspPayment.getCreationInstant().toInstant();

    var status = toPaymentStatus(volaPayment.getVerificationStatus());

    return volaPspPayment == null
        ? Payment.builder()
            .id(tsinjoId)
            .status(status)
            .pspLastVerificationInstant(lastVerificationInstant)
            .creationInstant(null)
            .build()
        : Payment.builder()
            .id(tsinjoId)
            .amount(volaPspPayment.getAmount())
            .pspType(toPspType(volaPspPayment.getPspType()))
            .pspId(volaPspPayment.getId())
            .status(status)
            .pspLastVerificationInstant(lastVerificationInstant)
            .creationInstant(creationInstant)
            .build();
  }

  private PspType toPspType(PspPayment.PspTypeEnum volaPspType) {
    return switch (volaPspType) {
      case ORANGE_MONEY -> ORANGE_MONEY;
    };
  }

  private PaymentStatus toPaymentStatus(
      school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum
          volaPaymentStatus) {
    if (volaPaymentStatus == null) {
      return PaymentStatus.UNKNOWN;
    }

    return switch (volaPaymentStatus) {
      case VERIFYING -> PaymentStatus.VERIFYING;
      case SUCCEEDED -> PaymentStatus.CONFIRMED;
      case FAILED -> PaymentStatus.REFUSED;
    };
  }
}
