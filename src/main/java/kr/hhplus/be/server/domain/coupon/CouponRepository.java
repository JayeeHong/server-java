package kr.hhplus.be.server.domain.coupon;

import java.util.List;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Coupon findById(Long id);

    Coupon findByIdWithPessimisticLock(Long id);

    List<Coupon> findAllAvailable();

    void deleteById(Long id);
}
