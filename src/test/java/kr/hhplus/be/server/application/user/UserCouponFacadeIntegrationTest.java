package kr.hhplus.be.server.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserCommand.Create;
import kr.hhplus.be.server.domain.user.UserCouponCommand;
import kr.hhplus.be.server.domain.user.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class UserCouponFacadeIntegrationTest {

    @Autowired
    private UserCouponFacade userCouponFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserCouponService userCouponService;

    UserInfo.User user;

    CouponInfo.Coupon coupon;

    @BeforeEach
    void setUp() {
        user = userService.createUser(Create.of("userA"));
        coupon = couponService.saveCoupon(CouponCommand.Create.of("couponA", 1000, 100,
            LocalDateTime.of(9999, 12, 31, 23, 59, 59)));
    }

    @Test
    @DisplayName("사용자가 보유한 쿠폰 목록을 조회한다")
    void getUserCoupons() {

        // given
        CouponInfo.Coupon coupon2 = couponService.saveCoupon(
            CouponCommand.Create.of("couponB", 2000, 1000,
                LocalDateTime.of(9999, 12, 31, 23, 59, 59)));


        UserCouponCommand.Publish command1 = UserCouponCommand.Publish.of(user.getUserId(), coupon.getCouponId());
        UserCouponCommand.Publish command2 = UserCouponCommand.Publish.of(user.getUserId(), coupon2.getCouponId());

        UserCouponInfo.Coupon userCoupon1 = userCouponService.createUserCoupon(command1);
        UserCouponInfo.Coupon userCoupon2 = userCouponService.createUserCoupon(command2);

        // when
        UserCouponResult.Coupons userCoupons = userCouponFacade.getUserCoupons(user.getUserId());

        // then
        assertThat(userCoupons.getCoupons()).hasSize(2)
            .extracting("userCouponId", "couponName", "discountAmount")
            .containsExactlyInAnyOrder(
                tuple(userCoupon1.getUserCouponId(), coupon.getName(), coupon.getDiscountAmount()),
                tuple(userCoupon2.getUserCouponId(), coupon2.getName(), coupon2.getDiscountAmount())
            );
    }

    @Test
    @DisplayName("사용자에게 쿠폰을 발급한다")
    void publishUserCoupon() {

        // given
        UserCouponCriteria.Publish criteria = UserCouponCriteria.Publish.of(user.getUserId(), coupon.getCouponId());

        // when
        userCouponFacade.publishUserCoupon(criteria);

        // then
        UserCouponInfo.Coupon userCoupon = userCouponService.getUserCoupon(user.getUserId(), coupon.getCouponId());
        assertThat(userCoupon).isNotNull();
        assertThat(userCoupon.getUserId()).isEqualTo(user.getUserId());
        assertThat(userCoupon.getCouponId()).isEqualTo(coupon.getCouponId());
        assertThat(userCoupon.getUsedAt()).isNull();
    }
}