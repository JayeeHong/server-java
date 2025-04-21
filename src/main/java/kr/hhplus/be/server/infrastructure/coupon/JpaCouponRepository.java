package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.stock > 0 AND c.expiredAt > CURRENT_TIMESTAMP")
    List<Coupon> findAllAvailable();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    Coupon findByIdWithPessimisticLock(Long couponId);
}
