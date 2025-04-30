package kr.hhplus.be.server.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PaymentTest {

    @Test
    @DisplayName("결제 금액은 0보다 커야 한다")
    void createWithInvalidAmount() {
        // when, then
        assertThatThrownBy(() -> Payment.create(1L, 0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COMPLETE", "FAIL", "CANCEL"})
    @DisplayName("결제 가능 상태에서 결제 가능하다")
    void payWithPayableStatus(PaymentStatus status) {

        // given
        Payment payment = Payment.of(1L, 1L, 1000, PaymentMethod.UNKNOWN, status, LocalDateTime.now());

        // when, then
        assertThatThrownBy(payment::pay)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("결제를 성공한다")
    void pay() {

        // given
        Payment payment = Payment.create(1L, 1000);

        // when
        payment.pay();

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETE);
    }
}