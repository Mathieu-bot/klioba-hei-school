package school.hei.tsinjo.model.psp.vola;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.tsinjo.model.PaymentStatus.CONFIRMED;
import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.tsinjo.conf.FacadeIT;

class VolaPspIT extends FacadeIT {
  @Autowired VolaPsp volaPsp;

  @Test
  void read_succeeded_payment() {
    var volaPayment =
        volaPsp.get(
            "d1b7f126-677f-4dfb-b871-87b5efcd70e7",
            ORANGE_MONEY,
            "MP250729.1216.D77954",
            "lou@hei.school");

    assertEquals(324_000, volaPayment.amount());
    assertEquals(CONFIRMED, volaPayment.status());
  }
}
