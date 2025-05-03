package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponCommand {

    @Getter
    public static class Create {

        private final String name;
        private final int discountAmount;
        private final int quantity;
        private final LocalDateTime expiredAt;

        private Create(String name, int discountAmount, int quantity, LocalDateTime expiredAt) {
            this.name = name;
            this.discountAmount = discountAmount;
            this.quantity = quantity;
            this.expiredAt = expiredAt;
        }

        public static Create of(String name, int discountAmount, int quantity, LocalDateTime expiredAt) {
            return new Create(name, discountAmount, quantity, expiredAt);
        }
    }

}
