package kr.hhplus.be.server.infrastructure.user;

import java.util.List;
import kr.hhplus.be.server.domain.user.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserId(Long userId);

}
