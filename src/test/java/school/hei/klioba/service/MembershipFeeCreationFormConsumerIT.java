package school.hei.klioba.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.klioba.model.PaymentStatus.VERIFYING;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.klioba.conf.FacadeIT;
import school.hei.klioba.endpoint.http.model.MembershipFeeCreationForm;
import school.hei.klioba.model.Event;

class MembershipFeeCreationFormConsumerIT extends FacadeIT {

  @Autowired MembershipFeeCreationFormConsumer membershipFeeCreationFormConsumer;
  @Autowired EventService eventService;

  private String generateValidPspId() {
    return "MP250811.1103.C" + String.format("%05d", (int) (Math.random() * 99999));
  }

  @Test
  void donate_then_read_donations() {
    var ref1 = generateValidPspId();
    var ref2 = generateValidPspId();
    var newEmail = randomUUID() + "@cute.dev";

    membershipFeeCreationFormConsumer.accept(
        new MembershipFeeCreationForm("Lou", "Andria", ref1), newEmail, "club1");
    membershipFeeCreationFormConsumer.accept(
        new MembershipFeeCreationForm(null, null, ref2), newEmail, "club1");

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

    membershipFeeCreationFormConsumer.accept(
        new MembershipFeeCreationForm("Lou", "Andria", pspId), "lou@cute.dev", "club1");

    assertThrows(
        IllegalArgumentException.class,
        () ->
            membershipFeeCreationFormConsumer.accept(
                new MembershipFeeCreationForm(null, null, pspId), "lou@cute.dev", "club1"));
  }

  @Test
  void donation_with_invalid_pspId_shouldFail() {
    String invalidPspId = randomUUID().toString();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            membershipFeeCreationFormConsumer.accept(
                new MembershipFeeCreationForm("Lou", "Andria", invalidPspId),
                "lou@cute.dev",
                "club1"));
  }
}
