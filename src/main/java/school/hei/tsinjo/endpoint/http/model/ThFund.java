package school.hei.tsinjo.endpoint.http.model;

import static school.hei.tsinjo.model.PaymentStatus.CONFIRMED;

import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.Payment;

@Getter
public class ThFund {
  private final List<Event> events;
  private final int allConfirmedDonations;
  private final int allConfirmedHelps;
  private final int confirmedRemainingFund;

  public ThFund(List<Event> events) {
    this.events = events;
    this.allConfirmedDonations = allConfirmedWithAmountPredicate(events, amount -> amount > 0);
    this.allConfirmedHelps = allConfirmedWithAmountPredicate(events, amount -> amount < 0);
    this.confirmedRemainingFund =
        // addition since helps are negative
        allConfirmedDonations + allConfirmedHelps;
  }

  private int allConfirmedWithAmountPredicate(
      List<Event> events, Predicate<Integer> amountPredicate) {
    return events.stream()
        .map(Event::getPayment)
        .filter(p -> CONFIRMED.equals(p.status()))
        .map(Payment::amount)
        .filter(amount -> amount != null && amountPredicate.test(amount))
        .mapToInt(Integer::intValue)
        .sum();
  }

  @Override
  public String toString() {
    return String.format(
        "Donations confirmées: %d Ar. "
            + "Aides confirmées: %d Ar. "
            + "Fonds restants confirmés: %d Ar.",
        allConfirmedDonations, allConfirmedHelps, confirmedRemainingFund);
  }
}
