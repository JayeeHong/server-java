package kr.hhplus.be.server.application.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    public CouponResponse.UserCoupon issueCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크

        // 쿠폰 식별자 유효성 체크
        // 쿠폰 발행 종료일 체크
        // 수량 남았는지 체크

        // 사용자에게 쿠폰 발행

        // 쿠폰 이력 추가

        return new CouponResponse.UserCoupon(1L, "COU20250403", 20, CouponType.PERCENTAGE,
            false, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public List<CouponResponse.UserCoupon> getCoupons(long userId) {

        // 사용자 식별자 유효성 체크

        // 쿠폰 목록 조회

        return List.of(
            new CouponResponse.UserCoupon(1L, "COU20250403001", 20, CouponType.PERCENTAGE,
                false, System.currentTimeMillis(), System.currentTimeMillis()),
            new CouponResponse.UserCoupon(1L, "COU20250403002", 1000, CouponType.FIXED_AMOUNT,
                false, System.currentTimeMillis(), System.currentTimeMillis())
        );
    }

    public CouponResponse.UserCoupon getCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크

        // 사용자의 쿠폰 조회
        
        return new CouponResponse.UserCoupon(1L, "COU20250403", 20, CouponType.PERCENTAGE,
            false, System.currentTimeMillis(), System.currentTimeMillis());
    }

}
