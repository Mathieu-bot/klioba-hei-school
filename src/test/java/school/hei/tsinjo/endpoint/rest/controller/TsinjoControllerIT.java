package school.hei.tsinjo.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.repository.EventRepository;
import school.hei.tsinjo.service.DonationCreationFormConsumer;

class TsinjoControllerIT extends FacadeIT {

  @Autowired DonationCreationFormConsumer donationCreationFormConsumer;
  @Autowired EventRepository eventRepository;

  @Test
  void donate_then_read_donations() {
    donationCreationFormConsumer.accept(
        new DonationCreationForm("lou@cute.dev", "Lou", "Andria", "orangeRef1"));
    donationCreationFormConsumer.accept(
        new DonationCreationForm("lou@cute.dev", null, null, "orangeRef2"));

    var events = eventRepository.findAll();
    assertEquals(2, events.size());
    var payment1 =
        events.stream()
            .map(Event::getPayment)
            .filter(p -> "orangeRef1".equals(p.pspId()))
            .findFirst()
            .get();
    assertEquals(VERIFYING, payment1.status());
    assertNull(payment1.amount());
    assertNull(payment1.pspLastVerificationInstant());
    var user =
        events.stream()
            .filter(e -> "orangeRef2".equals(e.getPayment().pspId()))
            .map(Event::getUser)
            .findFirst()
            .get();
    assertEquals("Lou", user.getFirstName());
    assertEquals("Andria", user.getLastName());
  }
}
