package school.hei.tsinjo.model;

import java.time.Instant;
import java.util.Date;
import school.hei.tsinjo.model.psp.PspType;

public record Payment(
    String id,
    Integer amount,
    PspType pspType,
    String pspId,
    PaymentStatus status,
    Instant pspLastVerificationInstant,
    Date creationInstant) {}
