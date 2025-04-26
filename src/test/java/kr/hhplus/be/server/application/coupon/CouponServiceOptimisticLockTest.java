//package kr.hhplus.be.server.application.coupon;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import jakarta.persistence.OptimisticLockException;
//import java.time.LocalDateTime;
//import kr.hhplus.be.server.domain.coupon.Coupon;
//import kr.hhplus.be.server.domain.coupon.CouponRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//class CouponServiceOptimisticLockTest {
//
//    @Autowired
//    private CouponRepository couponRepository;
//
//    @Test
//    @DisplayName("Optimistic Lock 충돌 테스트")
//    void optimisticLockConflictTest() {
//        // given
//        Coupon coupon = Coupon.of(null, "1000원 할인", 1000, 5, LocalDateTime.now()); // stock=5
//        couponRepository.save(coupon);
//
//        // when
//        Coupon couponA = getCoupon(coupon.getId());
//        Coupon couponB = getCoupon(coupon.getId());
//
//        // 트랜잭션 A: 저장
//        updateCoupon(couponA);
//
//        // then
//        // B가 저장하려고 하면 OptimisticLockException 발생해야 함
//        assertThatThrownBy(() -> updateCoupon(couponB))
//            .isInstanceOf(OptimisticLockException.class);
//    }
//
//    public Coupon getCoupon(long couponId) {
//        Coupon coupon = couponRepository.findById(couponId);
//        if (coupon == null) {
//            throw new IllegalArgumentException("쿠폰 없음");
//        }
//
//        return coupon;
//    }
//
//    public void updateCoupon(Coupon coupon) {
//        coupon.issue(); // 재고 감소
//        couponRepository.save(coupon);
//    }
//}