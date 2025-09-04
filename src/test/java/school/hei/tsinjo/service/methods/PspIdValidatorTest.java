package school.hei.tsinjo.service.methods;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import school.hei.tsinjo.service.DonationCreationFormConsumer;

public class PspIdValidatorTest {
  private final DonationCreationFormConsumer consumer =
      new DonationCreationFormConsumer(null, null, null, null);

  @Test
  void validPspIds_shouldReturnTrue() {
    assertTrue(consumer.isPspIdFormat("MP250811.1103.C03008"));
    assertTrue(consumer.isPspIdFormat("MP250811.1941.D18347"));
    assertTrue(consumer.isPspIdFormat("MP250829.1325.A80322"));
  }

  @Test
  void invalidPrefix_shouldReturnFalse() {
    assertFalse(consumer.isPspIdFormat("XP250811.1103.C03008"));
    assertFalse(consumer.isPspIdFormat("mp250811.1103.C03008"));
  }

  @Test
  void invalidDateOrHour_shouldReturnFalse() {
    assertFalse(consumer.isPspIdFormat("MP25081.1103.C03008"));
    assertFalse(consumer.isPspIdFormat("MP250811.110.C03008"));
    assertFalse(consumer.isPspIdFormat("MP250811.11032.C03008"));
  }

  @Test
  void invalidLastPart_shouldReturnFalse() {
    assertFalse(consumer.isPspIdFormat("MP250811.1103.03008"));
    assertFalse(consumer.isPspIdFormat("MP250811.1103.c03008"));
    assertFalse(consumer.isPspIdFormat("MP250811.1103.C0300"));
    assertFalse(consumer.isPspIdFormat("MP250811.1103.C030089"));
  }

  @Test
  void nullOrEmpty_shouldReturnFalse() {
    assertFalse(consumer.isPspIdFormat(null));
    assertFalse(consumer.isPspIdFormat(""));
    assertFalse(consumer.isPspIdFormat("   "));
  }
}
