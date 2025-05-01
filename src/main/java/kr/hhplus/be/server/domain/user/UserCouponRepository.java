package kr.hhplus.be.server.domain.user;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface UserCouponRepository {

    UserCoupon findByUserIdAndCouponId(Long userId, Long couponId);

    UserCoupon save(UserCoupon userCoupon);

    UserCoupon findById(Long id);

    List<UserCoupon> findByUserIdAndUsable(Long userId);

}
