package kr.hhplus.be.server.application.user;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCouponResult {

    @Getter
    public static class Coupons {

        private final List<Coupon> coupons;

        private Coupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }

        public static Coupons of(List<Coupon> coupons) {
            return new Coupons(coupons);
        }
    }

    @Getter
    public static class Coupon {

        private final Long userCouponId;
        private final String couponName;
        private final int discountAmount;

        private Coupon(Long userCouponId, String couponName, int discountAmount) {
            this.userCouponId = userCouponId;
            this.couponName = couponName;
            this.discountAmount = discountAmount;
        }

        public static Coupon of(Long userCouponId, String couponName, int discountAmount) {
            return new Coupon(userCouponId, couponName, discountAmount);
        }
    }
}
