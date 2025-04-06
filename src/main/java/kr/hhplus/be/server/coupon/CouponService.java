package kr.hhplus.be.server.coupon;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    public UserCoupon issueCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크

        // 쿠폰 식별자 유효성 체크
        // 쿠폰 발행 종료일 체크
        // 수량 남았는지 체크

        // 사용자에게 쿠폰 발행

        // 쿠폰 이력 추가

        return new UserCoupon(1L, "COU20250403", 20, CouponType.PERCENTAGE,
            false, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public List<UserCoupon> getCoupons(long userId) {

        // 사용자 식별자 유효성 체크

        // 쿠폰 목록 조회

        return List.of(
            new UserCoupon(1L, "COU20250403001", 20, CouponType.PERCENTAGE,
                false, System.currentTimeMillis(), System.currentTimeMillis()),
            new UserCoupon(1L, "COU20250403002", 1000, CouponType.FIXED_AMOUNT,
                false, System.currentTimeMillis(), System.currentTimeMillis())
        );
    }

    public UserCoupon getCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크

        // 사용자의 쿠폰 조회
        
        return new UserCoupon(1L, "COU20250403", 20, CouponType.PERCENTAGE,
            false, System.currentTimeMillis(), System.currentTimeMillis());
    }

}
