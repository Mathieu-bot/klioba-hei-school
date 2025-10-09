package school.hei.tsinjo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.model.psp.PspType;

public class DonationFormServiceTest {

  private EventService eventService;
  private DonationFormService donationFormService;

  @BeforeEach
  void setUp() {
    eventService = mock(EventService.class);
    donationFormService = new DonationFormService(eventService);
  }

  @Test
  void getPrefilledDonationForm_noPreviousEvent_returnsEmptyForm() {
    // Arrange
    String email = "user@example.com";
    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of());

    // Act
    DonationCreationForm form = donationFormService.getPrefilledDonationForm(email);

    // Assert
    assertEquals("", form.firstName());
    assertEquals("", form.lastName());
    assertEquals("", form.pspId());
  }

  @Test
  void getPrefilledDonationForm_hasPreviousEvent_returnsPrefilledForm() {
    // Arrange
    String email = "user@example.com";

    // Créer de vrais objets pour test
    User user = new User("1", "Tiavina", "Andriamamivony", email);
    Payment payment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "MP240201.1234.A12345",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.parse("2025-08-11T13:51:36.165532Z"));
    Donation donation = new Donation("d1", payment, user, Instant.now());

    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of(donation));

    // Act
    DonationCreationForm form = donationFormService.getPrefilledDonationForm(email);

    // Assert
    assertEquals("Tiavina", form.firstName());
    assertEquals("Andriamamivony", form.lastName());
    assertEquals("", form.pspId());
  }

  @Test
  void getPrefilledDonationForm_multipleEvents_returnsLatestForUser() {
    // Arrange
    String email = "user@example.com";

    User user1 = new User("1", "Alice", "Smith", email);
    Payment payment1 =
        new Payment(
            "p1",
            500,
            PspType.ORANGE_MONEY,
            "PSP1",
            PaymentStatus.CONFIRMED,
            Instant.now().minusSeconds(3600),
            Instant.parse("2025-08-11T13:51:36.165532Z"));
    Donation donation1 = new Donation("d1", payment1, user1, Instant.now().minusSeconds(3600));

    User user2 = new User("2", "Tiavina", "Andriamamivony", email);
    Payment payment2 =
        new Payment(
            "p2",
            1000,
            PspType.ORANGE_MONEY,
            "PSP2",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.parse("2025-08-11T13:51:36.165532Z"));
    Donation donation2 = new Donation("d2", payment2, user2, Instant.now());

    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of(donation1, donation2));

    // Act
    DonationCreationForm form = donationFormService.getPrefilledDonationForm(email);

    // Assert: doit prendre le dernier event de l'utilisateur
    assertEquals("Tiavina", form.firstName());
    assertEquals("Andriamamivony", form.lastName());
    assertEquals("", form.pspId());
  }
}
