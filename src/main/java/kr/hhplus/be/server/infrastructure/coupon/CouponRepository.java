package kr.hhplus.be.server.infrastructure.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponHistory;
import kr.hhplus.be.server.domain.user.UserCoupon;

public interface CouponRepository {

    Coupon findById(long couponId);

    List<UserCoupon> findAllByUserId(long userId);

    CouponHistory save(UserCoupon userCoupon);

    UserCoupon findByUserIdAndCouponId(long userId, long couponId);

}
