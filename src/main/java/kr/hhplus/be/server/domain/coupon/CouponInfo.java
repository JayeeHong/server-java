package kr.hhplus.be.server.domain.coupon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponInfo {

    @Getter
    public static class Coupon {

        private final Long couponId;
        private final String name;
        private final int discountAmount;

        private Coupon(Long couponId, String name, int discountAmount) {
            this.couponId = couponId;
            this.name = name;
            this.discountAmount = discountAmount;
        }

        public static Coupon of(Long couponId, String name, int discountAmount) {
            return new Coupon(couponId, name, discountAmount);
        }

        public static Coupon toCouponInfo(kr.hhplus.be.server.domain.coupon.Coupon coupon) {
            return Coupon.of(coupon.getId(), coupon.getName(), coupon.getDiscountAmount());
        }
    }

}
