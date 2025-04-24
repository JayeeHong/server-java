
package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
class CouponServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    CouponRepository couponRepository = mock(CouponRepository.class);
    UserCouponRepository userCouponRepository = mock(UserCouponRepository.class);
    CouponService couponService = new CouponService(userRepository, couponRepository, userCouponRepository);

    @Test
    @DisplayName("쿠폰 발급에 성공한다")
    void issueCoupon_success() {
        long userId = 1L;
        long couponId = 10L;

        User user = mock(User.class);
        Coupon coupon = Coupon.of(couponId, "1000원 할인", 1000, 10, LocalDateTime.now());
        coupon.issue();
        UserCoupon savedUserCoupon = UserCoupon.create(userId, couponId, LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(user);
        when(couponRepository.findByIdWithPessimisticLock(couponId)).thenReturn(coupon);
        when(couponRepository.save(coupon)).thenReturn(coupon);
        when(userCouponRepository.save(any())).thenReturn(savedUserCoupon);

        CouponResponse.UserCoupon result = couponService.issueCoupon(userId, couponId);

//        assertEquals(couponId, result.getCouponId());
//        assertEquals("1000원 할인", result.getCouponName());
//        assertEquals(1000, result.getDiscountAmount());
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게는 쿠폰을 발급할 수 없다")
    void issueCoupon_invalidUser() {
        when(userRepository.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
            couponService.issueCoupon(999L, 1L)
        );
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰은 발급할 수 없다")
    void issueCoupon_invalidCoupon() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(mock(User.class));
        when(couponRepository.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
            couponService.issueCoupon(userId, 999L)
        );
    }

    @Test
    @DisplayName("사용자 보유 쿠폰 목록을 조회할 수 있다")
    void getUserCoupons_success() {
        long userId = 1L;
        Coupon coupon = Coupon.of(10L, "할인쿠폰", 500, 5, LocalDateTime.now());
        UserCoupon userCoupon = UserCoupon.create(userId, coupon.getId(), LocalDateTime.now());

        when(userCouponRepository.findAllByUserId(userId)).thenReturn(List.of(userCoupon));

        List<CouponResponse.UserCoupon> result = couponService.getUserCoupons(userId);

        assertEquals(1, result.size());
//        assertEquals("할인쿠폰", result.get(0).getCouponName());
    }

    // TODO 쿠폰 사용 성공 TEST 작성
    @Test
    @DisplayName("쿠폰 사용에 성공한다")
    void useCoupon_success() {
        long userId = 1L;
        long couponId = 10L;

        User user = mock(User.class);
        Coupon coupon = Coupon.of(couponId, "3000원 할인", 3000, 5,
            LocalDateTime.of(9999, 4, 15, 20, 48));
        coupon.issue();
        List<UserCoupon> userCoupons = List.of(
            UserCoupon.create(userId, couponId, LocalDateTime.now()));

        when(userRepository.findById(userId)).thenReturn(user);
        when(userCouponRepository.findAllByUserId(userId)).thenReturn(userCoupons);
        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(couponRepository.save(any())).thenReturn(coupon);
        when(userCouponRepository.save(any())).thenReturn(UserCoupon.create(userId, couponId, LocalDateTime.now()));

        // TODO 쿠폰 사용
//        Coupon result = couponService.useCoupon(userId, couponId);

//        assertEquals(couponId, couponId);
//        assertEquals("3000원 할인", result.getName());
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게는 쿠폰을 사용할 수 없다")
    void useCoupon_invalidUser() {
        when(userRepository.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
            couponService.useCoupon(999L, 1L)
        );
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰은 사용할 수 없다")
    void useCoupon_invalidCoupon() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(mock(User.class));
        when(couponRepository.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
            couponService.useCoupon(userId, 999L)
        );
    }
}
