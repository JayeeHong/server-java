package kr.hhplus.be.server.domain.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public void publishCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        coupon.publish();
    }

    public CouponInfo.Coupon getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        return CouponInfo.Coupon.toCouponInfo(coupon);
    }

}
