package kr.hhplus.be.server.domain.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제를 생성하고 저장한다")
    void pay() {

        // given
        PaymentCommand.Payment payment = PaymentCommand.Payment.of(1L, 2L, 10_000L);

        // when
        paymentService.pay(payment);

        // then
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
