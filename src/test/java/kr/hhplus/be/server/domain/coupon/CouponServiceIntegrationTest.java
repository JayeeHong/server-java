package kr.hhplus.be.server.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.CouponCommand.Create;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰이 존재해야 쿠폰 발급이 가능하다")
    void issueCouponShouldCouponExist() {

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰이 발급 상태가 아니면 쿠폰을 발급할 수 없다")
    void cannotIssueCouponWithInvalidStatus() {

        // given
        Create command = Create.of("couponA", 1000, 100, LocalDateTime.of(9999, 12, 31, 23, 59));
        CouponInfo.Coupon savedCoupon = couponService.saveCoupon(command);

        couponService.expireCoupon(savedCoupon.getCouponId()); //쿠폰 만료 상태로 업데이트

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(savedCoupon.getCouponId()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰 수량이 부족하면 쿠폰을 발급할 수 없다")
    void cannotIssueCouponNotEnoughQuantity() {

        // given
        Create command = Create.of("couponA", 1000, 1, LocalDateTime.of(9999, 12, 31, 23, 59));
        CouponInfo.Coupon savedCoupon = couponService.saveCoupon(command);

        couponService.publishCoupon(savedCoupon.getCouponId()); // 쿠폰 발급 상태로 업데이트
        couponService.issueCoupon(savedCoupon.getCouponId());

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(savedCoupon.getCouponId()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰 번호가 유효하지 않으면 쿠폰을 조회할 수 없다")
    void cannotGetCouponWithInvalidId() {

        // when, then
        assertThatThrownBy(() -> couponService.getCoupon(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰을 조회한다")
    void getCoupon() {

        // given
        Create command = Create.of("couponA", 1000, 1, LocalDateTime.of(9999, 12, 31, 23, 59));
        CouponInfo.Coupon savedCoupon = couponService.saveCoupon(command);

        // when
        CouponInfo.Coupon findCoupon = couponService.getCoupon(savedCoupon.getCouponId());

        // then
        assertThat(findCoupon.getCouponId()).isEqualTo(savedCoupon.getCouponId());
    }
}
