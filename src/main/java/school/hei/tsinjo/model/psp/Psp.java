package school.hei.tsinjo.model.psp;

import school.hei.tsinjo.model.Payment;

public interface Psp {
  Payment create(String tsinjoId, PspType pspType, String pspId, String email);

  Payment get(String tsinjoId, PspType pspType, String pspId, String email);
}
