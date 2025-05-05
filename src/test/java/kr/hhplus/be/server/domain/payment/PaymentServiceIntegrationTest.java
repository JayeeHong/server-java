package kr.hhplus.be.server.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("주문을 결제한다")
    void pay() {

        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.of(1L, 10L, 10_000L);

        // when
        PaymentInfo.Payment savedPayment = paymentService.pay(command);

        // then
        PaymentInfo.Payment findPayment = paymentService.getPay(savedPayment.getPaymentId());
        assertThat(findPayment.getPaymentId()).isEqualTo(savedPayment.getPaymentId());
        assertThat(findPayment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETE);
    }
}
