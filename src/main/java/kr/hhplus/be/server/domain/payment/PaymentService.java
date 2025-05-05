package kr.hhplus.be.server.domain.payment;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentInfo.Payment pay(PaymentCommand.Payment command) {
        Payment payment = Payment.create(command.getOrderId(), command.getAmount());
        payment.pay();

        paymentRepository.save(payment);
        return PaymentInfo.Payment.of(payment.getId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus());
    }

    public PaymentInfo.Payment getPay(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("결제정보를 찾을 수 없습니다."));

        return PaymentInfo.Payment.of(payment.getId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus());
    }
}
