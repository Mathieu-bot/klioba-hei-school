package school.hei.tsinjo.endpoint.http.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.tsinjo.model.PaymentStatus.CONFIRMED;
import static school.hei.tsinjo.model.PaymentStatus.REFUSED;
import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;
import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Help;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.User;

class ThEventTest {

  @Test
  void confirmed_donation_to_string() {
    var payment =
        new Payment(
            "paymentId",
            17,
            ORANGE_MONEY,
            "pspId",
            CONFIRMED,
            Instant.parse("2025-08-11T13:51:26.165532Z"));
    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Donation("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals(
        "2025-08-11 16:51:16, 17 Ar. Par Lou Andria<lou@hei.school>. Statut: en succès, récupéré le"
            + " 2025-08-11 16:51:26. Payé via ORANGE_MONEY, réf: pspId.",
        thEvent.toString());
    assertEquals("black", thEvent.color());
  }

  @Test
  void confirmed_help_to_string() {
    var payment =
        new Payment(
            "paymentId",
            -17,
            ORANGE_MONEY,
            "pspId",
            CONFIRMED,
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Help("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals(
        "2025-08-11 16:51:16, -17 Ar. Pour Lou Andria<lou@hei.school>. ", thEvent.toString());
    assertEquals("blue", thEvent.color());
  }

  @Test
  void verifying_withNo_lastPspVerificationInstant_to_string() {
    var payment = new Payment("paymentId", null, ORANGE_MONEY, "pspId", VERIFYING, null);
    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Donation("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals(
        "2025-08-11 16:51:16. Par Lou Andria<lou@hei.school>. Statut: en vérification, récupéré le"
            + " --. Payé via ORANGE_MONEY, réf: pspId.",
        thEvent.toString());
    assertEquals("lightgray", thEvent.color());
  }

  @Test
  void refused_with_lastPspVerificationInstant_to_string() {
    var payment =
        new Payment(
            "paymentId",
            null,
            ORANGE_MONEY,
            "pspId",
            REFUSED,
            Instant.parse("2025-08-11T13:51:36.165532Z"));

    var user = new User("userId", "Lou", "Andria", "lou@hei.school");

    var thEvent =
        new ThEvent(
            new Donation("eventId", payment, user, Instant.parse("2025-08-11T13:51:16.165532Z")));

    assertEquals(
        "2025-08-11 16:51:16. Par Lou Andria<lou@hei.school>. Statut: en échec, récupéré le"
            + " 2025-08-11 16:51:36. Payé via ORANGE_MONEY, réf: pspId.",
        thEvent.toString());
    assertEquals("red", thEvent.color());
  }
}
