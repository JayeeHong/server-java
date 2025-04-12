package kr.hhplus.be.server.domain.user;

import java.util.List;

public interface UserCouponRepository {

    UserCoupon save(UserCoupon userCoupon);

    UserCoupon findById(Long id);

    List<UserCoupon> findAllByUserId(Long userId);
}
