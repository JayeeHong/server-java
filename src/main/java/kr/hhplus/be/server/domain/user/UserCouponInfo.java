package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCouponInfo {

    @Getter
    public static class UsableCoupon {
        private final Long userCouponId;

        private UsableCoupon(Long userCouponId) {
            this.userCouponId = userCouponId;
        }

        public static UsableCoupon of(Long userCouponId) {
            return new UsableCoupon(userCouponId);
        }
    }

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
        private final Long userId;
        private final Long couponId;
        private final LocalDateTime issuedAt;
        private final LocalDateTime usedAt;

        private Coupon(Long userCouponId, Long userId, Long couponId, LocalDateTime issuedAt, LocalDateTime usedAt) {
            this.userCouponId = userCouponId;
            this.userId = userId;
            this.couponId = couponId;
            this.issuedAt = issuedAt;
            this.usedAt = usedAt;
        }

        public static Coupon of(Long userCouponId, Long userId, Long couponId, LocalDateTime issuedAt, LocalDateTime usedAt) {
            return new Coupon(userCouponId, userId, couponId, issuedAt, usedAt);
        }

        public static Coupon toUserCouponInfo(UserCoupon userCoupon) {
            return new Coupon(
                userCoupon.getId(),
                userCoupon.getUserId(),
                userCoupon.getCouponId(),
                userCoupon.getIssuedAt(),
                userCoupon.getUsedAt());
        }
    }
}
