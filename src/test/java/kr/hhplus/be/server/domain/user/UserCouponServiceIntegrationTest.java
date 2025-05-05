package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import kr.hhplus.be.server.domain.user.UserCouponCommand.Publish;
import kr.hhplus.be.server.domain.user.UserCouponCommand.UsableCoupon;
import kr.hhplus.be.server.domain.user.UserCouponInfo.Coupon;
import kr.hhplus.be.server.domain.user.UserCouponInfo.Coupons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class UserCouponServiceIntegrationTest {

    @Autowired
    private UserCouponService userCouponService;

    @Test
    @DisplayName("사용자가 이미 발급받은 쿠폰은 중복 발급받을 수 없다")
    void cannotCreateDuplicateUserCoupon() {

        // given
        UserCouponCommand.Publish command = UserCouponCommand.Publish.of(1L, 100L);
        userCouponService.createUserCoupon(command);

        // when
        assertThatThrownBy(() -> userCouponService.createUserCoupon(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자에게 쿠폰을 발급한다")
    void createUserCoupon() {

        // given
        Publish command = Publish.of(1L, 100L);

        // when
        Coupon coupon = userCouponService.createUserCoupon(command);

        // then
        assertThat(coupon.getUserCouponId()).isNotNull();
    }

    @Test
    @DisplayName("사용자가 발급 받은 쿠폰이 없으면 사용 가능한 쿠폰을 조회할 수 없다")
    void cannotGetUserCouponWithoutCreateUserCoupon() {

        // given
        UsableCoupon command = UsableCoupon.of(1L, 100L);

        // when, then
        assertThat(userCouponService.getUsableCoupon(command)).isNull();
    }

    @Test
    @DisplayName("사용한 쿠폰은 조회할 수 없다")
    void cannotGetUsedUserCoupon() {

        // given
        Publish command = Publish.of(1L, 100L);
        Coupon coupon = userCouponService.createUserCoupon(command);

        userCouponService.useUserCoupon(coupon.getUserCouponId());

        UsableCoupon usableCommand = UsableCoupon.of(command.getUserId(), command.getCouponId());

        // when, then
        assertThatThrownBy(() -> userCouponService.getUsableCoupon(usableCommand))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용할 수 있는 쿠폰을 조회한다")
    void getUsableUserCoupon() {

        // given
        Publish publishCommand = Publish.of(1L, 100L);
        Coupon coupon = userCouponService.createUserCoupon(publishCommand);

        UsableCoupon command = UsableCoupon.of(publishCommand.getUserId(),
            publishCommand.getCouponId());

        // when
        UserCouponInfo.UsableCoupon usableCoupon = userCouponService.getUsableCoupon(command);

        // then
        assertThat(usableCoupon.getUserCouponId()).isEqualTo(coupon.getUserCouponId());
    }

    @Test
    @DisplayName("사용자가 발급받지 않은 쿠폰은 사용할 수 없다")
    void cannotUseNotPublish() {

        // when, then
        assertThatThrownBy(() -> userCouponService.useUserCoupon(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용할 수 없는 쿠폰은 사용할 수 없다")
    void cannotUseUsedUserCoupon() {

        // given
        Publish publishCommand = Publish.of(1L, 100L);
        Coupon coupon = userCouponService.createUserCoupon(publishCommand);

        userCouponService.useUserCoupon(coupon.getUserCouponId());

        UsableCoupon command = UsableCoupon.of(publishCommand.getUserId(),
            publishCommand.getCouponId());

        // when, then
        assertThatThrownBy(() -> userCouponService.useUserCoupon(coupon.getUserCouponId()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용할 수 있는 쿠폰을 사용한다")
    void useUserCoupon() {

        // given
        Publish publishCommand = Publish.of(1L, 100L);
        Coupon coupon = userCouponService.createUserCoupon(publishCommand);

        // when
        userCouponService.useUserCoupon(coupon.getUserCouponId());

        // then
        Coupon findCoupon = userCouponService.getUserCouponById(coupon.getUserCouponId());
        assertThat(findCoupon.getIssuedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자의 쿠폰 목록을 조회한다")
    void getUserCoupons() {

        // given
        UserCoupon userCoupon = UserCoupon.create(1L, 100L);
        Publish publishCommand = Publish.of(userCoupon.getUserId(), userCoupon.getCouponId());
        Coupon coupon = userCouponService.createUserCoupon(publishCommand);

        UserCoupon anotherUserCoupon = UserCoupon.create(2L, 100L);
        Publish anotherPublishCommand = Publish.of(anotherUserCoupon.getUserId(), anotherUserCoupon.getCouponId());
        userCouponService.createUserCoupon(anotherPublishCommand);

        UserCoupon usedUserCoupon = UserCoupon.create(1L, 200L);
        Publish usedPublishCommand = Publish.of(usedUserCoupon.getUserId(), usedUserCoupon.getCouponId());
        Coupon usedCoupon = userCouponService.createUserCoupon(usedPublishCommand);
        userCouponService.useUserCoupon(usedCoupon.getUserCouponId());

        // when
        Coupons coupons = userCouponService.getUserCoupons(1L);

        // then
        assertThat(coupons.getCoupons()).hasSize(1)
            .extracting("userCouponId", "userId", "couponId")
            .containsExactlyInAnyOrder(
                tuple(coupon.getUserCouponId(), coupon.getUserId(), coupon.getCouponId())
            );
    }
}
