package kr.hhplus.be.server.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.user.UserCouponResult.Coupons;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCouponFacadeUnitTest {

    @InjectMocks
    private UserCouponFacade userCouponFacade;

    @Mock
    private UserService userService;

    @Mock
    private CouponService couponService;

    @Mock
    private UserCouponService userCouponService;

    @Test
    @DisplayName("사용자가 보유한 쿠폰 목록을 조회한다")
    void getUserCoupons() {

        // given
        UserCouponInfo.Coupons coupons = mock(UserCouponInfo.Coupons.class);

        when(coupons.getCoupons())
            .thenReturn(List.of(
                UserCouponInfo.Coupon.of(1L, 10L, 100L, LocalDateTime.of(2025, 5, 1, 10, 0, 0), null),
                UserCouponInfo.Coupon.of(2L, 10L, 200L, LocalDateTime.of(2025, 5, 1, 10, 0, 0), null))
            );

        when(userCouponService.getUserCoupons(anyLong()))
            .thenReturn(coupons);

        when(couponService.getCoupon(anyLong()))
            .thenReturn(CouponInfo.Coupon.of(100L, "couponA", 1000));

        // when
        Coupons userCoupons = userCouponFacade.getUserCoupons(10L);

        // then
        InOrder inOrder = inOrder(userCouponService, couponService);
        inOrder.verify(userCouponService, times(1)).getUserCoupons(anyLong());
        inOrder.verify(couponService, times(2)).getCoupon(anyLong());

        assertThat(userCoupons.getCoupons())
            .extracting("userCouponId", "couponName", "discountAmount")
            .containsExactlyInAnyOrder(
                tuple(1L, "couponA", 1_000),
                tuple(2L, "couponA", 1_000)
            );
    }

    @Test
    @DisplayName("사용자 쿠폰을 발급한다")
    void issueUserCoupon() {

        // given
        UserCouponCriteria.Publish criteria = mock(UserCouponCriteria.Publish.class);

        // when
        userCouponFacade.issueUserCoupon(criteria);

        // then
        InOrder inOrder = inOrder(userService, couponService, userCouponService);
        inOrder.verify(userService, times(1)).getUser(criteria.getUserId());
        inOrder.verify(couponService, times(1)).publishCoupon(criteria.getCouponId());
        inOrder.verify(userCouponService, times(1)).createUserCoupon(criteria.toCommand());
    }
}