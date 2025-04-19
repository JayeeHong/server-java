package kr.hhplus.be.server.infrastructure.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    
    private final JpaCouponRepository jpaCouponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }

    @Override
    public Coupon findById(Long id) {
        return jpaCouponRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));
    }

    @Override
    public List<Coupon> findAllAvailable() {
        return jpaCouponRepository.findAllAvailable();
    }

    @Override
    public void deleteById(Long id) {
        jpaCouponRepository.deleteById(id);
    }
}
