package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private long amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paidAt;

    private Payment(Long id, Long orderId, long amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
    }

    public static Payment of(Long id, Long orderId, long amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime paidAt) {
        return new Payment(id, orderId, amount, paymentMethod, paymentStatus, paidAt);
    }

    public static Payment create(Long orderId, long amount) {
        validateAmount(amount);

        return Payment.of(null, orderId, amount, PaymentMethod.UNKNOWN, PaymentStatus.READY, null);
    }

    public void pay() {
        if (paymentStatus.cannotPay()) {
            throw new IllegalStateException("결제 가능한 상태가 아닙니다.");
        }

        this.paymentStatus = PaymentStatus.COMPLETE;
        this.paidAt = LocalDateTime.now();
    }

    private static void validateAmount(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다.");
        }
    }

}
