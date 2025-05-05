package kr.hhplus.be.server.domain.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInfo {

    @Getter
    public static class Payment {
        private final Long paymentId;
        private final long amount;
        private final PaymentMethod paymetMethod;
        private final PaymentStatus paymentStatus;

        private Payment(Long paymentId, long amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
            this.paymentId = paymentId;
            this.amount = amount;
            this.paymetMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
        }

        public static Payment of(Long paymentId, long amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
            return new Payment(paymentId, amount, paymentMethod, paymentStatus);
        }
    }
}
