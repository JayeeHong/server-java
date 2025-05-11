package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.config.redis.DistributedLock;
import kr.hhplus.be.server.config.redis.LockType;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponFacade {

    private final UserService userService;
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    public UserCouponResult.Coupons getUserCoupons(Long userId) {
        userService.getUser(userId);

        List<UserCouponResult.Coupon> coupons = userCouponService.getUserCoupons(userId).getCoupons().stream()
            .map(this::getUserCoupon)
            .toList();

        return UserCouponResult.Coupons.of(coupons);
    }

    @Transactional
    @DistributedLock(type = LockType.COUPON, key = "#criteria.couponId")
    public void issueUserCoupon(UserCouponCriteria.Publish criteria) {
        userService.getUser(criteria.getUserId());

        couponService.issueCoupon(criteria.getCouponId());
        userCouponService.createUserCoupon(criteria.toCommand());
    }

    private UserCouponResult.Coupon getUserCoupon(UserCouponInfo.Coupon userCoupon) {
        CouponInfo.Coupon coupon = couponService.getCoupon(userCoupon.getCouponId());

        return UserCouponResult.Coupon.of(userCoupon.getUserCouponId(), coupon.getName(), coupon.getDiscountAmount());
    }
}
