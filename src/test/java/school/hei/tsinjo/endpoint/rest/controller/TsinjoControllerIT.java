package school.hei.tsinjo.endpoint.rest.controller;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;
import school.hei.tsinjo.repository.jpa.JEventRepository;
import school.hei.tsinjo.repository.jpa.JPaymentRepository;
import school.hei.tsinjo.repository.jpa.JUserRepository;
import school.hei.tsinjo.repository.jpa.model.JEvent;
import school.hei.tsinjo.repository.jpa.model.JPayment;
import school.hei.tsinjo.repository.jpa.model.JUser;

class TsinjoControllerIT extends FacadeIT {

  @Autowired TsinjoController tsinjoController;
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

    var jPayment = new JPayment();
    jPayment.setId(randomUUID().toString());
    jPayment.setAmount(17);
    jPayment.setCreationInstant(now());
    jPaymentRepository.save(jPayment);

    var jEvent = new JEvent();
    jEvent.setId(randomUUID().toString());
    jEvent.setUser(jUser);
    jEvent.setPayment(jPayment);
    jEventRepository.save(jEvent);

    var events = tsinjoController.getEvents();

    assertEquals(1, events.size());
  }
}
