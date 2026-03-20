package school.hei.tsinjo.endpoint.http.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.tsinjo.model.PaymentStatus.UNKNOWN;
import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Help;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.User;

class ThEventAdditionalTest {

  @Test
  void unknown_status_to_string() {
    var payment =
        new Payment(
            "paymentId",
            null,
            ORANGE_MONEY,
            "pspId",
            UNKNOWN,
            Instant.parse("2025-08-11T13:51:36.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Donation("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals(
        "2025-08-11 16:51:16. Par Lou Andria<lou@hei.school>. Statut: inconnu, récupéré le"
            + " 2025-08-11 16:51:36. Payé via ORANGE_MONEY, réf: pspId.",
        thEvent.toString());
    assertEquals("yellow", thEvent.color());
  }

  @Test
  void unknown_status_with_negativeAmount_hasBlueColor() {
    var payment =
        new Payment(
            "paymentId",
            -100,
            ORANGE_MONEY,
            "pspId",
            UNKNOWN,
            Instant.parse("2025-08-11T13:51:36.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Help("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z"), ""));

    assertEquals("yellow", thEvent.color());
  }

  @Test
  void confirmed_status_with_zeroAmount_hasBlackColor() {
    var payment =
        new Payment(
            "paymentId",
            0,
            ORANGE_MONEY,
            "pspId",
            school.hei.tsinjo.model.PaymentStatus.CONFIRMED,
            Instant.parse("2025-08-11T13:51:36.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Donation("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals("black", thEvent.color());
  }

  @Test
  void help_without_statusDetails_hasNoPaymentInfo() {
    var payment =
        new Payment(
            "paymentId",
            -500,
            ORANGE_MONEY,
            "pspId",
            school.hei.tsinjo.model.PaymentStatus.CONFIRMED,
            Instant.parse("2025-08-11T13:51:36.165532Z"),
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Alice", "Smith", "alice@example.com");

    var thEvent =
        new ThEvent(
            new Help(
                "eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z"), "OUT"));

    assertEquals(
        "2025-08-11 16:51:16, -500 Ar. Pour Alice Smith<alice@example.com>. ", thEvent.toString());
  }
}
