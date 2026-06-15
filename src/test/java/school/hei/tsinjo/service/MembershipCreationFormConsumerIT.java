package school.hei.tsinjo.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.endpoint.http.model.MembershipCreationForm;
import school.hei.tsinjo.model.Event;

class MembershipCreationFormConsumerIT extends FacadeIT {

  @Autowired MembershipCreationFormConsumer membershipCreationFormConsumer;
  @Autowired EventService eventService;

  private String generateValidPspId() {
    return "MP250811.1103.C" + String.format("%05d", (int) (Math.random() * 99999));
  }

  @Test
  void donate_then_read_donations() {
    var ref1 = generateValidPspId();
    var ref2 = generateValidPspId();
    var newEmail = randomUUID() + "@cute.dev";

    membershipCreationFormConsumer.accept(
        new MembershipCreationForm("Lou", "Andria", ref1), newEmail);
    membershipCreationFormConsumer.accept(new MembershipCreationForm(null, null, ref2), newEmail);

    var events = eventService.findAllWithPaymentResolution();
    assertEquals(2, events.size());

    var payment1 =
        events.stream()
            .map(Event::getPayment)
            .filter(p -> ref1.equals(p.pspId()))
            .findFirst()
            .orElseThrow();
    assertEquals(VERIFYING, payment1.status());
    assertNull(payment1.amount());
    assertNull(payment1.pspLastVerificationInstant());

    var user =
        events.stream()
            .filter(e -> ref2.equals(e.getPayment().pspId()))
            .map(Event::getUser)
            .findFirst()
            .orElseThrow();
    assertEquals("Lou", user.getFirstName());
    assertEquals("Andria", user.getLastName());
  }

  @Test
  void donations_cannot_have_same_pspId() {
    String pspId = generateValidPspId();

    membershipCreationFormConsumer.accept(
        new MembershipCreationForm("Lou", "Andria", pspId), "lou@cute.dev");

    assertThrows(
        IllegalArgumentException.class,
        () ->
            membershipCreationFormConsumer.accept(
                new MembershipCreationForm(null, null, pspId), "lou@cute.dev"));
  }

  @Test
  void donation_with_invalid_pspId_shouldFail() {
    String invalidPspId = randomUUID().toString();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            membershipCreationFormConsumer.accept(
                new MembershipCreationForm("Lou", "Andria", invalidPspId), "lou@cute.dev"));
  }
}
