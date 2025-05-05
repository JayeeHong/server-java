package kr.hhplus.be.server.interfaces.user;

import java.util.List;
import kr.hhplus.be.server.application.user.UserCouponResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCouponResponse {

    @Getter
    @NoArgsConstructor
    public static class Coupons {

        private List<CouponTest> coupons;

        private Coupons(List<CouponTest> coupons) {
            this.coupons = coupons;
        }

        public static Coupons of(UserCouponResult.Coupons coupons) {
            if (coupons == null) {
                System.out.println(coupons);
            }

            return new Coupons(coupons.getCoupons().stream()
                .map(CouponTest::of)
                .toList()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CouponTest {

        private Long id;
        private String name;
        private long discountAmount;

        private CouponTest(Long id, String name, long discountAmount) {
            this.id = id;
            this.name = name;
            this.discountAmount = discountAmount;
        }

        public static CouponTest of(UserCouponResult.Coupon coupon) {
            return new CouponTest(coupon.getUserCouponId(), coupon.getCouponName(), coupon.getDiscountAmount());
        }
    }
}
