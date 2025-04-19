package kr.hhplus.be.server.infrastructure.user;

import java.util.List;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public List<UserCoupon> findAllByUserId(Long userId) {
        return jpaUserCouponRepository.findAllByUserId(userId);
    }

    @Override
    public UserCoupon findByUserIdAndCouponId(Long userId, Long couponId) {
        return jpaUserCouponRepository.findByUserIdAndCouponId(userId, couponId);
    }
}
