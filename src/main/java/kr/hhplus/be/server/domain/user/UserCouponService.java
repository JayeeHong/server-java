package kr.hhplus.be.server.domain.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    // 사용자 쿠폰을 발행한다
    public UserCouponInfo.Coupon createCoupon(UserCouponCommand.Publish command) {
        UserCoupon findCoupon = userCouponRepository.findByUserIdAndCouponId(command.getUserId(),
            command.getCouponId());

        if (findCoupon != null) {
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCoupon userCoupon = UserCoupon.create(command.getUserId(), command.getCouponId());
        userCouponRepository.save(userCoupon);

        return UserCouponInfo.Coupon.toUserCouponInfo(userCoupon);
    }

    // 사용할 수 있는 쿠폰을 조회한다
    public UserCouponInfo.UsableCoupon getUsableCoupon(UserCouponCommand.UsableCoupon command) {
        UserCoupon findCoupon = userCouponRepository.findByUserIdAndCouponId(command.getUserId(),
            command.getCouponId());

        if (findCoupon == null) {
            return null;
        }

        if (findCoupon.cannotUse()) {
            throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
        }

        return UserCouponInfo.UsableCoupon.of(findCoupon.getId());
    }

    // 쿠폰을 사용한다
    public void useUserCoupon(Long userCouponId) {
        UserCoupon findCoupon = userCouponRepository.findById(userCouponId);
        findCoupon.use();
    }

    // 사용자의 쿠폰 목록을 조회한다
    public UserCouponInfo.Coupons getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndUsable(userId);

        return UserCouponInfo.Coupons.of(userCoupons.stream()
            .map(UserCouponInfo.Coupon::toUserCouponInfo)
            .toList());
    }

    // 사용자 쿠폰 ID로 쿠폰을 조회한다
    public UserCouponInfo.Coupon getUserCouponById(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId);

        return UserCouponInfo.Coupon.toUserCouponInfo(userCoupon);
    }
}
