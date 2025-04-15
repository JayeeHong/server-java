package kr.hhplus.be.server.domain.user;

import java.util.List;

public interface UserCouponRepository {

    UserCoupon save(UserCoupon userCoupon);

    List<UserCoupon> findAllByUserId(Long userId);
}
