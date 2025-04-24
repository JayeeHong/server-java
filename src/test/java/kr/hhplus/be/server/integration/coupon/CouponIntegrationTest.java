package kr.hhplus.be.server.integration.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse.UserCoupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
public class CouponIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private Coupon couponA;
    private Coupon couponB;

    private User user;

    @BeforeEach
    void setUp() {
        couponA = Coupon.of(null, "1000원 할인", 1000, 100,
            LocalDateTime.of(9999, 12, 31, 23, 59));
        couponRepository.save(couponA);

        couponB = Coupon.of(null, "2000원 할인", 2000, 100,
            LocalDateTime.of(9999, 12, 31, 23, 59));
        couponRepository.save(couponB);
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    void issueCouponTest() {

        // given
        user = User.create("userA");
        userRepository.save(user);

        // when
        UserCoupon userCoupon = couponService.issueCoupon(user.getId(), couponA.getId());
        List<kr.hhplus.be.server.domain.user.UserCoupon> userCoupons = userCouponRepository.findAllByUserId(
            user.getId());

        // then
//        assertThat(userCoupon.getCouponId()).isEqualTo(couponA.getId());
//        assertThat(userCoupon.getDiscountAmount()).isEqualTo(couponA.getDiscountAmount());
//        assertThat(userCoupons).hasSize(1);
    }

    @Test
    @DisplayName("쿠폰 발급 실패 - 같은 사용자에게 두번 발급")
    void issueCouponFailTest() {

        // given
        user = User.create("userA");
        userRepository.save(user);

        // when
        couponService.issueCoupon(user.getId(), couponA.getId());
        assertThatThrownBy(() -> couponService.issueCoupon(user.getId(), couponA.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("이미 발급받은 쿠폰입니다.");
    }

    @Test
    @DisplayName("사용자 보유 쿠폰 목록 조회")
    void getUserCouponsTest() {

        // given
        user = User.create("userA");
        userRepository.save(user);

        couponService.issueCoupon(user.getId(), couponA.getId());
        couponService.issueCoupon(user.getId(), couponB.getId());

        // when
        List<UserCoupon> userCoupons = couponService.getUserCoupons(user.getId());

        // then
        assertThat(userCoupons).hasSize(2);
    }
}
