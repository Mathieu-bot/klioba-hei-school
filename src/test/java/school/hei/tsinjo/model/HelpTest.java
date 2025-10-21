package school.hei.tsinjo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.model.psp.PspType;

class HelpTest {

  @Test
  void help_creation_succeeds() {
    var payment =
        new Payment(
            "p1",
            -1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "Jane", "Doe", "jane@example.com");
    Instant creationInstant = Instant.now();

    var help = new Help("h1", payment, user, creationInstant);

    assertNotNull(help);
    assertEquals("h1", help.getId());
    assertEquals(payment, help.getPayment());
    assertEquals(user, help.getUser());
    assertEquals(creationInstant, help.getCreationInstant());
  }

  @Test
  void help_toString_works() {
    // Arrange
    var payment =
        new Payment(
            "p1",
            -1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.parse("2025-08-11T13:51:26.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));
    var user = new User("u1", "Jane", "Doe", "jane@example.com");
    var help = new Help("h1", payment, user, Instant.now());

    assertNotNull(help.toString());
  }
}
