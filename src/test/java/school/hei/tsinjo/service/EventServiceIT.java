package school.hei.tsinjo.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static school.hei.tsinjo.model.PaymentStatus.CONFIRMED;
import static school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum.SUCCEEDED;
import static school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum.VERIFYING;
import static school.hei.tsinjo.model.psp.vola.api.gen.client.model.PspPayment.PspTypeEnum.ORANGE_MONEY;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.psp.vola.api.VolaClient;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.PspPayment;

class EventServiceIT extends FacadeIT {
  @Autowired DonationCreationFormConsumer donationCreationFormConsumer;
  @Autowired EventService eventService;
  @MockBean VolaClient volaClientMock;

  private String generateValidPspId() {
    return "MP250811.1103.C" + String.format("%05d", (int) (Math.random() * 99999));
  }

  @Transactional
  @Rollback
  @Test
  void create_then_confirm() {
    var ref1 = generateValidPspId();
    var newEmail = randomUUID() + "@cute.dev";

    var verifyingVolaPayment = aVolaPayment(VERIFYING);
    when(volaClientMock.create(any(), eq(ref1), eq(newEmail))).thenReturn(verifyingVolaPayment);
    donationCreationFormConsumer.accept(new DonationCreationForm("Lou", "Andria", ref1), newEmail);

    // Just after creation, we simulate that Vola still replies with VERIFYING
    when(volaClientMock.get(any(), any(), any())).thenReturn(verifyingVolaPayment);
    var events = eventService.findAllWithPaymentResolution();
    assertEquals(1, events.size());
    assertEquals(PaymentStatus.VERIFYING, events.get(0).getPayment().status());

    // Now we simulate Vola replies with SUCCEEDED
    var succeededVolaPayment = aVolaPayment(SUCCEEDED);
    when(volaClientMock.get(any(), any(), any())).thenReturn(succeededVolaPayment);
    events = eventService.findAllWithPaymentResolution();
    assertEquals(1, events.size());
    assertEquals(CONFIRMED, events.get(0).getPayment().status());
  }

  private static Payment aVolaPayment(VerificationStatusEnum status) {
    var verifyingVolaPspPayment = new PspPayment();
    verifyingVolaPspPayment.setId(randomUUID().toString());
    verifyingVolaPspPayment.setPspType(ORANGE_MONEY);

    var volaPaymentMock = mock(Payment.class);
    when(volaPaymentMock.getVerificationStatus()).thenReturn(status);
    when(volaPaymentMock.getPspPayment()).thenReturn(verifyingVolaPspPayment);
    return volaPaymentMock;
  }
}
