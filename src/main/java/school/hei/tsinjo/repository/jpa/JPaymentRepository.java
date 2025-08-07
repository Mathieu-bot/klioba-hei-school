package school.hei.tsinjo.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.tsinjo.repository.jpa.model.JPayment;

@Repository
public interface JPaymentRepository extends JpaRepository<JPayment, String> {}
