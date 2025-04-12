package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    /**
     * 선착순 쿠폰 발급
     */
    public CouponResponse.UserCoupon issueCoupon(long userId, long couponId) {

        // 사용자 유효성 확인
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        // 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId);
        if (coupon == null) {
            throw new IllegalArgumentException("유효하지 않는 쿠폰입니다.");
        }

        // 재고 감소 처리
        Coupon issued = coupon.issue();

        // 쿠폰 재고 저장
        couponRepository.save(issued);

        // 사용자 쿠폰 발급 기록 저장
        UserCoupon userCoupon = UserCoupon.issue(userId, issued, LocalDateTime.now());
        UserCoupon saved = userCouponRepository.save(userCoupon);

        // 응답 변환
        return CouponResponse.UserCoupon.from(saved);
    }

    /**
     * 사용자 보유 쿠폰 목록 조회
     */
    public List<CouponResponse.UserCoupon> getUserCoupons(long userId) {
        return userCouponRepository.findAllByUserId(userId).stream()
                .map(CouponResponse.UserCoupon::from)
                .toList();
    }

    /**
     * 쿠폰 사용
     */
    public Coupon validateAndUseCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        Coupon coupon = couponRepository.findById(couponId);
        if (coupon == null) {
            throw new IllegalArgumentException("유효하지 않은 쿠폰입니다.");
        }

        Coupon issued = coupon.issue(); // 재고 차감
        couponRepository.save(issued); // 반영

        UserCoupon userCoupon = UserCoupon.issue(userId, issued, LocalDateTime.now());
        userCouponRepository.save(userCoupon); // 사용자 쿠폰 저장

        return issued;
    }

}
