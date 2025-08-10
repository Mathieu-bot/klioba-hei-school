package school.hei.tsinjo.model;

import java.time.Instant;

public record Payment(
    String id,
    Integer amount,
    PspType pspType,
    String pspId,
    PaymentStatus status,
    Instant pspLastVerificationInstant) {}
