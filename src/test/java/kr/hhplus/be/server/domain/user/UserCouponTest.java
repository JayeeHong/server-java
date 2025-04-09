package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCode;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.Discount;
import kr.hhplus.be.server.domain.coupon.IssueCount;
import kr.hhplus.be.server.domain.coupon.IssuePeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

    private Coupon createValidCoupon() {
        return new Coupon(
            1L,
            new CouponCode("WELCOME10"),
            new Discount(1000),
            new IssuePeriod(LocalDateTime.now().plusDays(1)),
            new IssueCount(10),
            CouponType.PERCENTAGE,
            101L
        );
    }

    private Coupon createExpiredCoupon() {
        return new Coupon(
            2L,
            new CouponCode("EXPIRED10"),
            new Discount(500),
            new IssuePeriod(LocalDateTime.now().minusDays(1)),
            new IssueCount(5),
            CouponType.PERCENTAGE,
            102L
        );
    }

    @Test
    void 유저쿠폰_정상_생성() {
        Coupon coupon = createValidCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());

        assertFalse(userCoupon.isUsed());
        assertTrue(userCoupon.isAvailable());
        assertEquals(100L, userCoupon.getUserId());
    }

    @Test
    void 쿠폰_정상_사용처리() {
        Coupon coupon = createValidCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());

        userCoupon.use();

        assertTrue(userCoupon.isUsed());
        assertNotNull(userCoupon.getUsedAt());
    }

    @Test
    void 이미_사용한_쿠폰_사용시_예외() {
        Coupon coupon = createValidCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());

        userCoupon.use();

        assertThrows(IllegalStateException.class, userCoupon::use);
    }

    @Test
    void 만료된_쿠폰_사용시_예외() {
        Coupon coupon = createExpiredCoupon();
        UserCoupon userCoupon = new UserCoupon(2L, 100L, coupon, LocalDateTime.now());

        assertFalse(userCoupon.isAvailable());
        assertThrows(IllegalStateException.class, userCoupon::use);
    }

    @Test
    void 할인_정상_적용() {
        Coupon coupon = createValidCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());

        int discounted = userCoupon.applyDiscount(5000);
        assertEquals(4000, discounted);
    }

    @Test
    void 만료된_쿠폰_할인적용시_예외() {
        Coupon coupon = createExpiredCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());

        assertThrows(IllegalStateException.class, () -> userCoupon.applyDiscount(5000));
    }

    @Test
    void 이미_사용된_쿠폰_할인적용시_예외() {
        Coupon coupon = createValidCoupon();
        UserCoupon userCoupon = new UserCoupon(1L, 100L, coupon, LocalDateTime.now());
        userCoupon.use();

        assertThrows(IllegalStateException.class, () -> userCoupon.applyDiscount(5000));
    }
}
