package kr.hhplus.be.server.application.coupon;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.infrastructure.coupon.CouponRepository;
import kr.hhplus.be.server.infrastructure.user.UserCouponRepository;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponResponse.UserCoupon issueCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크
        User findUser = userRepository.findById(userId);
        if (findUser == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        // 쿠폰 식별자 유효성 체크
        Coupon findCoupon = couponRepository.findById(couponId);
        if (findCoupon == null) {
            throw new IllegalArgumentException("유효하지 않는 쿠폰입니다.");
        }

        // 쿠폰 발행
        findCoupon.issue();

        // 사용자에게 쿠폰 발행
        UserCoupon userCoupon = new UserCoupon(null, userId, findCoupon, LocalDateTime.now());
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        // 쿠폰 이력 추가
        couponRepository.save(savedUserCoupon);

        return userCoupon.translateUserCoupon();
    }

    public List<CouponResponse.UserCoupon> getCoupons(long userId) {

        // 사용자 식별자 유효성 체크
        User findUser = userRepository.findById(userId);
        if (findUser == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        // 쿠폰 목록 반환
        return couponRepository.findAllByUserId(userId).stream()
            .map(UserCoupon::translateUserCoupon)
            .map(userCoupon -> new CouponResponse.UserCoupon(
                userCoupon.userId(), userCoupon.couponCode(), userCoupon.discount(),
                userCoupon.type(), userCoupon.used(), userCoupon.issuedAt(),
                userCoupon.expiredAt()
            )).toList();
    }

    public CouponResponse.UserCoupon getCoupon(long userId, long couponId) {

        // 사용자 식별자 유효성 체크
        User findUser = userRepository.findById(userId);
        if (findUser == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        // 사용자의 쿠폰 조회
        couponRepository.findByUserIdAndCouponId(userId, couponId);

        return new CouponResponse.UserCoupon(1L, "COU20250403", 20, CouponType.PERCENTAGE,
            false, LocalDateTime.now(), LocalDateTime.now());
    }

}
