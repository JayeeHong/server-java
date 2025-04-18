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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    /**
     * 선착순 쿠폰 발급
     */
    @Transactional
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

        // 중복 발급 방지 (선택적으로 사용)
        boolean alreadyIssued = userCouponRepository.findAllByUserId(userId).stream()
            .anyMatch(uc -> uc.getCoupon().getId().equals(couponId));
        if (alreadyIssued) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        // 쿠폰 발급
        Coupon issued = coupon.issue();
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
    @Transactional
    public Coupon useCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        UserCoupon userCoupon = userCouponRepository.findAllByUserId(userId).stream()
            .filter(uc -> uc.getCoupon().getId().equals(couponId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰을 보유하고 있지 않습니다."));

        // 쿠폰 만료 여부 체크
        if (userCoupon.getCoupon().isExpired()) {
            throw new IllegalStateException("만료된 쿠폰입니다.");
        }

        // 사용 처리
        userCoupon.markAsUsed();
        userCouponRepository.save(userCoupon);

        return userCoupon.getCoupon();
    }

}
