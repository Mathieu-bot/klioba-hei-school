package school.hei.klioba.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.klioba.model.psp.PspType;

class EventTest {

  @Test
  void from_withPositiveAmount_createsDonation() {
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
    Instant creationInstant = Instant.now();

    var event = Event.from("e1", payment, user, creationInstant, "");

    assertInstanceOf(MembershipFee.class, event);
    assertEquals("e1", event.getId());
    assertEquals(payment, event.getPayment());
    assertEquals(user, event.getUser());
    assertEquals(creationInstant, event.getCreationInstant());
  }

  @Test
  void from_withNegativeAmount_createsHelp() {
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
    var creationInstant = Instant.now();

    var event = Event.from("e1", payment, user, creationInstant, "");

    assertInstanceOf(Withdrawal.class, event);
    assertEquals("e1", event.getId());
    assertEquals(payment, event.getPayment());
    assertEquals(user, event.getUser());
    assertEquals(creationInstant, event.getCreationInstant());
  }

  @Test
  void from_withNullAmount_createsDonation() {
    var payment =
        new Payment(
            "p1",
            null,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.VERIFYING,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "John", "Doe", "john@example.com");
    var creationInstant = Instant.now();

    var event = Event.from("e1", payment, user, creationInstant, "");

    assertInstanceOf(MembershipFee.class, event);
  }

  @Test
  void from_withZeroAmount_createsDonation() {
    var payment =
        new Payment(
            "p1",
            0,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "John", "Doe", "john@example.com");
    var creationInstant = Instant.now();

    var event = Event.from("e1", payment, user, creationInstant, "");

    assertInstanceOf(MembershipFee.class, event);
  }

  @Test
  void donation_withPayment_createsNewDonation() {
    var oldPayment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.VERIFYING,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "John", "Doe", "john@example.com");
    var creationInstant = Instant.now();
    var donation = new MembershipFee("d1", oldPayment, user, creationInstant);

    var newPayment =
        new Payment(
            "p2",
            1500,
            PspType.ORANGE_MONEY,
            "PSP456",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());

    var updatedEvent = donation.withPayment(newPayment);

    assertInstanceOf(MembershipFee.class, updatedEvent);
    assertEquals("d1", updatedEvent.getId());
    assertEquals(newPayment, updatedEvent.getPayment());
    assertEquals(user, updatedEvent.getUser());
    assertEquals(creationInstant, updatedEvent.getCreationInstant());
  }

  @Test
  void help_withPayment_createsNewHelp() {
    var oldPayment =
        new Payment(
            "p1",
            -1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());
    var user = new User("u1", "Jane", "Doe", "jane@example.com");
    var creationInstant = Instant.now();
    var help = new Withdrawal("h1", oldPayment, user, creationInstant, "");

    var newPayment =
        new Payment(
            "p2",
            -1500,
            PspType.ORANGE_MONEY,
            "PSP456",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());

    var updatedEvent = help.withPayment(newPayment);

    assertInstanceOf(Withdrawal.class, updatedEvent);
    assertEquals("h1", updatedEvent.getId());
    assertEquals(newPayment, updatedEvent.getPayment());
    assertEquals(user, updatedEvent.getUser());
    assertEquals(creationInstant, updatedEvent.getCreationInstant());
  }
}
