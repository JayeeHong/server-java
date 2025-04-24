package kr.hhplus.be.server.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

    @DisplayName("쿠폰 이름은 필수다.")
    @Test
    void isValidCouponNameTest() {
        // when, then
        assertThatThrownBy(() -> Coupon.create(null, 1000, 10, LocalDateTime.now().plusDays(1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 할인 금액은 0 이상이어야 한다.")
    @Test
    void isValidCouponDiscountAmountTest() {
        // when, then
        assertThatThrownBy(() -> Coupon.create("couponA", -1, 10, LocalDateTime.now().plusDays(1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 수량은 1 이상이어야 한다.")
    @Test
    void isValidCouponQuantityTest() {
        // when, then
        assertThatThrownBy(
            () -> Coupon.create("couponA", 1000, 0, LocalDateTime.now().plusDays(1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 만료일은 필수여야 한다.")
    @Test
    void isValidCouponExpiredAtTest() {
        // when, then
        assertThatThrownBy(() -> Coupon.create("couponA", 1000, 100, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 만료일은 현재 시간 이후여야 한다.")
    @Test
    void isValidCouponExpiredAtTest2() {
        // when, then
        assertThatThrownBy(
            () -> Coupon.create("couponA", 1000, 100, LocalDateTime.now().minusMinutes(10)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰은 '발급가능' 상태일 때 발급할 수 있다.")
    @Test
    void issueCouponTestCheckStatus() {
        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100, LocalDateTime.now().plusDays(10));
        coupon.expire();

        // when, then
        assertThatThrownBy(() -> coupon.issue()).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("쿠폰 수량이 충분할 때 발급할 수 있다.")
    @Test
    void issueCouponTestTestExpireAt() {
        // given
        Coupon coupon = Coupon.create("couponA", 1000, 1, LocalDateTime.now().plusDays(10));
        coupon.publish(); // 상태 변경
        coupon.issue();

        // when, then
        assertThatThrownBy(() -> coupon.issue()).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issueCouponTest() {
        // given
        Coupon coupon = Coupon.create("couponA", 1000, 1, LocalDateTime.now().plusDays(1));
        coupon.publish();

        // when
        coupon.issue();

        // then
        assertThat(coupon.getQuantity()).isEqualTo(0);
    }
}