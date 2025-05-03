package kr.hhplus.be.server.domain.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    // 쿠폰 발급
    @Transactional
    public void issueCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        coupon.issue();

        couponRepository.save(coupon);
    }

    // 쿠폰 발행 상태로 업데이트
    @Transactional
    public void publishCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        coupon.publish();

        couponRepository.save(coupon);
    }

    // 쿠폰 만료 상태로 업데이트
    @Transactional
    public void expireCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        coupon.expire();

        couponRepository.save(coupon);
    }

    public CouponInfo.Coupon getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId);
        return CouponInfo.Coupon.toCouponInfo(coupon);
    }

    @Transactional
    public CouponInfo.Coupon saveCoupon(CouponCommand.Create command) {

        Coupon coupon = Coupon.create(command.getName(), command.getDiscountAmount(),
            command.getQuantity(), command.getExpiredAt());
        couponRepository.save(coupon);

        return CouponInfo.Coupon.toCouponInfo(coupon);
    }

}
