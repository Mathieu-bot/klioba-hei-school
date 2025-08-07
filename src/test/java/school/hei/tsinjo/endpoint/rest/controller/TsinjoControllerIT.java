package school.hei.tsinjo.endpoint.rest.controller;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.repository.EventRepository;
import school.hei.tsinjo.repository.jpa.JEventRepository;
import school.hei.tsinjo.repository.jpa.JPaymentRepository;
import school.hei.tsinjo.repository.jpa.JUserRepository;
import school.hei.tsinjo.repository.jpa.model.JEvent;
import school.hei.tsinjo.repository.jpa.model.JPayment;
import school.hei.tsinjo.repository.jpa.model.JUser;

class TsinjoControllerIT extends FacadeIT {

  @Autowired EventRepository eventRepository;
  @Autowired JEventRepository jEventRepository;
  @Autowired JUserRepository jUserRepository;
  @Autowired JPaymentRepository jPaymentRepository;

  @Test
  void read_persisted_events() {
    var jUser = new JUser();
    jUser.setId(randomUUID().toString());
    jUser.setEmail("lou" + randomUUID() + "@cute.dev");
    jUser.setFirstName("Lou");
    jUser.setLastName("Andria");
    jUserRepository.save(jUser);

    var jDonation = new JPayment();
    jDonation.setId(randomUUID().toString());
    jDonation.setAmount(17);
    jDonation.setCreationInstant(now());
    jPaymentRepository.save(jDonation);

    var jDonationEvent = new JEvent();
    jDonationEvent.setId(randomUUID().toString());
    jDonationEvent.setUser(jUser);
    jDonationEvent.setPayment(jDonation);
    jEventRepository.save(jDonationEvent);

    var jHelp = new JPayment();
    jHelp.setId(randomUUID().toString());
    jHelp.setAmount(-17);
    jHelp.setCreationInstant(now());
    jPaymentRepository.save(jHelp);

    var jHelpEvent = new JEvent();
    jHelpEvent.setId(randomUUID().toString());
    jHelpEvent.setUser(jUser);
    jHelpEvent.setPayment(jHelp);
    jEventRepository.save(jHelpEvent);

    var events = eventRepository.findAll();

    assertEquals(2, events.size());

    var donation =
        events.stream().map(Event::getPayment).filter(p -> p.amount() > 0).findFirst().get();
    assertEquals(17, donation.amount());
  }
}
