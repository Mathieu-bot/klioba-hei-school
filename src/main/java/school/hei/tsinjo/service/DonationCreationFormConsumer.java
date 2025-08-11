package school.hei.tsinjo.service;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.VolaPsp;
import school.hei.tsinjo.repository.EventRepository;
import school.hei.tsinjo.repository.PaymentRepository;
import school.hei.tsinjo.repository.UserRepository;

@Service
@AllArgsConstructor
public class DonationCreationFormConsumer implements Consumer<DonationCreationForm> {
  private final UserRepository userRepository;
  private final PaymentRepository paymentRepository;
  private final EventRepository eventRepository;

  private final VolaPsp volaPsp;

  @Transactional
  @Override
  public void accept(DonationCreationForm donationCreationForm) {
    if (paymentRepository.findByPspId(donationCreationForm.pspId()).isPresent()) {
      // have to manually check since following volaPsp::create
      // will just return Bad Gateway, reverting the transaction
      // but without us knowing why
      throw new IllegalArgumentException("pspId already exists");
    }

    var paymentCreatedInVola =
        volaPsp.create(
            randomUUID().toString(),
            pspType(),
            donationCreationForm.pspId(),
            donationCreationForm.email());
    var payment = paymentRepository.save(paymentCreatedInVola);
    var user = userFrom(donationCreationForm);
    eventRepository.save(Event.from(randomUUID().toString(), payment, user, now()));
  }

  private static PspType pspType() {
    return switch (PspType.values()[0]) {
      case ORANGE_MONEY -> ORANGE_MONEY;
    };
  }

  private User userFrom(DonationCreationForm donationCreationForm) {
    return userRepository.saveIfEmailNotExist(
        donationCreationForm.firstName(),
        donationCreationForm.lastName(),
        donationCreationForm.email());
  }
}
