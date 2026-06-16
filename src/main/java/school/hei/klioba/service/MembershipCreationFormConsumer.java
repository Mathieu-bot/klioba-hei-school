package school.hei.klioba.service;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static school.hei.klioba.model.psp.PspType.ORANGE_MONEY;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.klioba.endpoint.http.model.MembershipCreationForm;
import school.hei.klioba.model.Event;
import school.hei.klioba.model.User;
import school.hei.klioba.model.psp.PspType;
import school.hei.klioba.model.psp.vola.VolaPsp;
import school.hei.klioba.repository.EventRepository;
import school.hei.klioba.repository.PaymentRepository;
import school.hei.klioba.repository.UserRepository;

@Service
@AllArgsConstructor
public class MembershipCreationFormConsumer {
  private final UserRepository userRepository;
  private final PaymentRepository paymentRepository;
  private final EventRepository eventRepository;

  private final VolaPsp volaPsp;

  @Transactional
  public void accept(MembershipCreationForm donationCreationForm, String email, String clubId) {
    if (paymentRepository.findByPspId(donationCreationForm.pspId()).isPresent()) {
      throw new IllegalArgumentException("pspId already exists");
    } else if (!isPspIdFormat(donationCreationForm.pspId())) {
      throw new IllegalArgumentException("pspId format incorrect format");
    }

    var paymentCreatedInVola =
        volaPsp.create(randomUUID().toString(), pspType(), donationCreationForm.pspId(), email);
    var payment = paymentRepository.save(paymentCreatedInVola);
    var user = userFrom(donationCreationForm, email);
    var club =
        clubRepository
            .findById(clubId)
            .orElseThrow(() -> new IllegalArgumentException("Club not found: " + clubId));
    eventRepository.save(Event.from(randomUUID().toString(), payment, user, club, now(), ""));
  }

  private static PspType pspType() {
    return switch (PspType.values()[0]) {
      case ORANGE_MONEY -> ORANGE_MONEY;
    };
  }

  private User userFrom(MembershipCreationForm donationCreationForm, String email) {
    return userRepository.saveIfEmailNotExist(
        donationCreationForm.firstName(), donationCreationForm.lastName(), email);
  }

  public boolean isPspIdFormat(String pspId) {
    if (pspId == null) {
      return false;
    }
    return pspId.matches("^MP\\d{6}\\.\\d{4}\\.[A-Z]\\d{5}$");
  }
}
