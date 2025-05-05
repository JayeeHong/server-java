package kr.hhplus.be.server.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제를 저장한다")
    void save() {

        // given
        Payment payment = Payment.create(1L, 10_000L);

        // when
        Payment savedPayment = paymentRepository.save(payment);

        // then
        assertThat(savedPayment.getId()).isNotNull();
    }

    @Test
    @DisplayName("결제정보를 조회한다")
    void findPayment() {

        // given
        Payment payment = Payment.create(1L, 10_000L);
        paymentRepository.save(payment);

        // when
        Optional<Payment> findPayment = paymentRepository.findById(payment.getId());

        // then
        assertThat(findPayment.get().getId()).isNotNull();
        assertThat(findPayment.get().getOrderId()).isEqualTo(payment.getOrderId());
    }
}