package school.hei.tsinjo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.model.psp.PspType;

class DonationTest {

  @Test
  void donation_creation_succeeds() {
    var payment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "John", "Doe", "john@example.com");
    var creationInstant = Instant.now();

    var donation = new Donation("d1", payment, user, creationInstant);

    assertNotNull(donation);
    assertEquals("d1", donation.getId());
    assertEquals(payment, donation.getPayment());
    assertEquals(user, donation.getUser());
    assertEquals(creationInstant, donation.getCreationInstant());
  }

  @Test
  void donation_toString_works() {
    // Arrange
    var payment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.parse("2025-08-11T13:51:26.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));
    var user = new User("u1", "John", "Doe", "john@example.com");
    var donation = new Donation("d1", payment, user, Instant.now());

    assertNotNull(donation.toString());
  }
}
