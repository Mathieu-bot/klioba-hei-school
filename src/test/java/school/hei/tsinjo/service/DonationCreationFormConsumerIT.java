package school.hei.tsinjo.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Event;

class DonationCreationFormConsumerIT extends FacadeIT {

  @Autowired DonationCreationFormConsumer donationCreationFormConsumer;
  @Autowired EventService eventService;

  @Test
  void donate_then_read_donations() {
    var ref1 = randomUUID().toString();
    var ref2 = randomUUID().toString();
    var newEmail = randomUUID() + "@cute.dev";
    donationCreationFormConsumer.accept(new DonationCreationForm("Lou", "Andria", ref1), newEmail);
    donationCreationFormConsumer.accept(new DonationCreationForm(null, null, ref2), newEmail);

    var events = eventService.findAllWithPaymentResolution();
    assertEquals(2, events.size());
    var payment1 =
        events.stream()
            .map(Event::getPayment)
            .filter(p -> ref1.equals(p.pspId()))
            .findFirst()
            .get();
    assertEquals(VERIFYING, payment1.status());
    assertNull(payment1.amount());
    assertNull(payment1.pspLastVerificationInstant());
    var user =
        events.stream()
            .filter(e -> ref2.equals(e.getPayment().pspId()))
            .map(Event::getUser)
            .findFirst()
            .get();
    assertEquals("Lou", user.getFirstName());
    assertEquals("Andria", user.getLastName());
  }

  @Test
  void donations_cannot_have_same_pspId() {
    String pspId = randomUUID().toString();
    donationCreationFormConsumer.accept(
        new DonationCreationForm("Lou", "Andria", pspId), "lou@cute.dev");
    assertThrows(
        IllegalArgumentException.class,
        () ->
            donationCreationFormConsumer.accept(
                new DonationCreationForm(null, null, pspId), "lou@cute.dev"));
  }
}
