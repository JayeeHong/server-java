package kr.hhplus.be.server.infrastructure.user;

import java.util.List;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public UserCoupon findByUserIdAndCouponId(Long userId, Long couponId) {
        return userCouponJpaRepository.findByUserIdAndCouponId(userId, couponId).orElse(null);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public UserCoupon findById(Long id) {
        return userCouponJpaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자가 보유한 쿠폰을 찾을 수 없습니다."));
    }

    @Override
    public List<UserCoupon> findByUserIdAndUsable(Long userId) {
        return userCouponJpaRepository.findByUserIdAndUsedAt(userId, null);
    }
}
