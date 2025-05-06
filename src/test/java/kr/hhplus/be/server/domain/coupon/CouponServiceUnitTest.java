package kr.hhplus.be.server.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CouponServiceUnitTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰ID가 유효하지 않으면 쿠폰을 발급할 수 없다")
    void cannotIssueWithInvalidId() {

        // given
        when(couponRepository.findByIdWithPessimisticLock(anyLong()))
            .thenThrow(new IllegalArgumentException("쿠폰을 발급할 수 없습니다."));

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(anyLong()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰이 발급 가능한 상태가 아니면 쿠폰을 발급할 수 없다")
    void cannotIssueWithInvalidStatus() {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100, LocalDateTime.of(9999, 12, 31, 23, 59));
        coupon.expire();

        when(couponRepository.findByIdWithPessimisticLock(anyLong())).thenReturn(coupon);

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(anyLong()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰 만료 기간이 지났으면 쿠폰을 발급할 수 없다")
    void cannotIssueAfterExpiredAt() {

        // given
        Coupon coupon = Coupon.of(1L, "couponA", 1000, 100, LocalDateTime.of(2025, 5, 1, 12, 00));
        coupon.publish();

        when(couponRepository.findByIdWithPessimisticLock(anyLong())).thenReturn(coupon);

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(anyLong()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰 수량이 충분하지 않으면 쿠폰을 발급할 수 없다")
    void cannotIssueNotEnoughQuantity() {

        // given
        Coupon coupon = Coupon.of(1L, "couponA", 1000, 0, LocalDateTime.of(9999, 12, 31, 23, 59));
        coupon.publish();

        when(couponRepository.findByIdWithPessimisticLock(anyLong())).thenReturn(coupon);

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(anyLong()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰을 발급한다")
    void issueCoupon() {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100, LocalDateTime.of(9999, 12, 31, 23, 59));
        coupon.publish();

        when(couponRepository.findByIdWithPessimisticLock(anyLong())).thenReturn(coupon);

        // when
        couponService.issueCoupon(anyLong());

        // then
        assertThat(coupon.getQuantity()).isEqualTo(99);
    }

    @Test
    @DisplayName("쿠폰ID가 유효하지 않으면 쿠폰을 조회할 수 없다")
    void getCouponWithInvalidId() {

        // given
        when(couponRepository.findById(anyLong()))
            .thenThrow(new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        // when, then
        assertThatThrownBy(() -> couponService.getCoupon(anyLong()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰을 조회한다")
    void getCoupon() {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100, LocalDateTime.of(9999, 12, 31, 23, 59));

        when(couponRepository.findById(anyLong())).thenReturn(coupon);

        // when
        CouponInfo.Coupon findCoupon = couponService.getCoupon(anyLong());

        // then
        assertThat(findCoupon.getName()).isEqualTo(coupon.getName());
    }
}
