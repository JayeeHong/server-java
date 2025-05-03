package kr.hhplus.be.server.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("유효하지 않은 ID로 쿠폰을 조회할 수 없다")
    void getCouponWithInvalidId() {

        // when, then
        assertThatThrownBy(() -> couponRepository.findById(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효한 ID로 쿠폰을 조회할 수 있다")
    void getCoupon() {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100,
            LocalDateTime.of(2025, 12, 31, 22, 00));
        couponRepository.save(coupon);

        // when
        Coupon savedCoupon = couponRepository.findById(coupon.getId());

        // then
        assertThat(savedCoupon.getId()).isEqualTo(coupon.getId());
    }
}
