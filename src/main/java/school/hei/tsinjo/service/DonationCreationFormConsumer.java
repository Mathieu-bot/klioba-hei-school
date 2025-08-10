package school.hei.tsinjo.service;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;
import static school.hei.tsinjo.model.PspType.ORANGE_MONEY;

import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PspType;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.repository.EventRepository;
import school.hei.tsinjo.repository.PaymentRepository;
import school.hei.tsinjo.repository.UserRepository;

@Service
@AllArgsConstructor
public class DonationCreationFormConsumer implements Consumer<DonationCreationForm> {
  private final UserRepository userRepository;
  private final PaymentRepository paymentRepository;
  private final EventRepository eventRepository;

  @Transactional
  @Override
  public void accept(DonationCreationForm donationCreationForm) {
    var payment = paymentRepository.save(paymentFrom(donationCreationForm));
    var user = userFrom(donationCreationForm);
    eventRepository.save(Event.from(randomUUID().toString(), payment, user, now()));
  }

  private User userFrom(DonationCreationForm donationCreationForm) {
    return userRepository.saveIfEmailNotExist(
        donationCreationForm.firstName(),
        donationCreationForm.lastName(),
        donationCreationForm.email());
  }

  private Payment paymentFrom(DonationCreationForm donationCreationForm) {
    return switch (PspType.values()[0]) {
      case ORANGE_MONEY ->
          new Payment(
              randomUUID().toString(),
              null,
              ORANGE_MONEY,
              donationCreationForm.orangePaymentReference(),
              VERIFYING,
              null);
    };
  }
}
