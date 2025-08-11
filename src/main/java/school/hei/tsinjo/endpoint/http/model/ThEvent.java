package school.hei.tsinjo.endpoint.http.model;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import school.hei.tsinjo.model.Event;

public record ThEvent(Event event) {

  private static final ZoneId ZONE_ID = ZoneId.of("UTC+3");

  @Override
  public String toString() {
    return String.format(
        "%s%s. %s. %s. %s",
        format(event.getCreationInstant()), amount(), user(), status(), paymentDetails());
  }

  public String color() {
    var payment = event.getPayment();
    return switch (payment.status()) {
      case VERIFYING -> "lightgray";
      case CONFIRMED -> payment.amount() >= 0 ? "black" : "blue";
      case REFUSED -> "red";
    };
  }

  private String paymentDetails() {
    var payment = event.getPayment();
    return String.format("Payé par %s, réf: %s.", payment.pspType(), payment.pspId());
  }

  private String status() {
    var payment = event.getPayment();
    return String.format(
        "Statut: %s, récupéré le %s",
        switch (event.getPayment().status()) {
          case VERIFYING -> "en vérification";
          case CONFIRMED -> "en succès";
          case REFUSED -> "en échec";
        },
        format(payment.pspLastVerificationInstant()));
  }

  private String user() {
    var user = event.getUser();
    return String.format("%s %s<%s>", user.getFirstName(), user.getLastName(), user.getEmail());
  }

  private String amount() {
    var amount = event.getPayment().amount();
    return amount == null ? "" : (", " + amount + " Ar");
  }

  private String format(Instant instant) {
    return instant == null
        ? "--"
        : instant.atZone(ZONE_ID).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
