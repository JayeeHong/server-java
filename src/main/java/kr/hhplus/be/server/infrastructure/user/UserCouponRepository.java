package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.UserCoupon;

public interface UserCouponRepository {

    UserCoupon save(UserCoupon userCoupon);

    UserCoupon findById(Long id);

}
