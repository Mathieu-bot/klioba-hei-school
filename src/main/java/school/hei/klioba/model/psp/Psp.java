package school.hei.klioba.model.psp;

import school.hei.klioba.model.Payment;

public interface Psp {
  Payment create(String tsinjoId, PspType pspType, String pspId, String email);

  Payment get(String tsinjoId, PspType pspType, String pspId, String email);
}
