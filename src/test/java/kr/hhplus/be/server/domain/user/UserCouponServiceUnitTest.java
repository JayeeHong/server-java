package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.user.UserCouponCommand.Publish;
import kr.hhplus.be.server.domain.user.UserCouponCommand.UsableCoupon;
import kr.hhplus.be.server.domain.user.UserCouponInfo.Coupons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceUnitTest {

    @InjectMocks
    private UserCouponService userCouponService;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Test
    @DisplayName("사용자에게 쿠폰을 발행한다")
    void createUserCoupon() {

        // given
        Publish command = mock(Publish.class);

        // when
        userCouponService.createCoupon(command);

        // then
        verify(userCouponRepository, times(1)).save(any(UserCoupon.class));
    }

    @Test
    @DisplayName("사용자에게 동일한 쿠폰을 여러번 발행할 수 없다")
    void createUserCouponCannotDuplicate() {

        // given
        UserCouponCommand.Publish command = mock(UserCouponCommand.Publish.class);
        UserCoupon userCoupon = UserCoupon.create(command.getUserId(), command.getCouponId());

        when(userCouponRepository.findByUserIdAndCouponId(anyLong(), anyLong()))
            .thenReturn(userCoupon);

        // when, then
        assertThatThrownBy(() -> userCouponService.createCoupon(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효한 ID로 사용 가능한 쿠폰을 조회해야 한다")
    void getUsableCouponWithValidUserIdAndCouponId() {

        // given
        UsableCoupon command = mock(UsableCoupon.class);

        when(userCouponRepository.findByUserIdAndCouponId(anyLong(), anyLong()))
            .thenThrow(new IllegalArgumentException("사용자가 보유한 쿠폰을 찾을 수 없습니다."));

        // when, then
        assertThatThrownBy(() -> userCouponService.getUsableCoupon(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용할 수 없는 쿠폰은 조회할 수 없다")
    void cannotGetUsedCoupon() {

        // given
        UserCouponCommand.UsableCoupon command = mock(UserCouponCommand.UsableCoupon.class);
        UserCoupon userCoupon = UserCoupon.create(1L, 100L);
        userCoupon.use();

        when(userCouponRepository.findByUserIdAndCouponId(anyLong(), anyLong()))
            .thenThrow(new IllegalStateException("사용할 수 없는 쿠폰입니다."));

        // when, then
        assertThatThrownBy(() -> userCouponService.getUsableCoupon(command))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용할 수 있는 쿠폰을 조회한다")
    void getCanUseCoupon() {

        // given
        UserCouponCommand.UsableCoupon command = mock(UserCouponCommand.UsableCoupon.class);
        UserCoupon userCoupon = UserCoupon.of(1L, 10L, 100L);

        when(userCouponRepository.findByUserIdAndCouponId(anyLong(), anyLong()))
            .thenReturn(userCoupon);

        // when
        UserCouponInfo.UsableCoupon usableCoupon = userCouponService.getUsableCoupon(command);

        // then
        assertThat(usableCoupon.getUserCouponId()).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 ID로 쿠폰을 사용할 수 없다")
    void cannotUseCouponWithInvalidId() {

        // given
        when(userCouponRepository.findById(anyLong()))
            .thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(() -> userCouponService.useUserCoupon(anyLong()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용 날짜가 있는 쿠폰은 사용할 수 없다")
    void cannotUseUsedAtIsNotNull() {

        // given
        UserCoupon userCoupon = UserCoupon.of(1L, 10L, 100L);
        userCoupon.use();

        when(userCouponRepository.findById(anyLong()))
            .thenReturn(userCoupon);

        // when, then
        assertThatThrownBy(() -> userCouponService.useUserCoupon(userCoupon.getId()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("쿠폰을 사용한다")
    void useUserCoupon() {

        // given
        UserCoupon userCoupon = UserCoupon.of(1L, 10L, 100L);

        when(userCouponRepository.findById(userCoupon.getId()))
            .thenReturn(userCoupon);

        // when
        userCouponService.useUserCoupon(userCoupon.getId());

        // then
        assertThat(userCoupon.getUsedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자가 보유한 쿠폰 목록을 조회한다")
    void getUserCoupons() {

        // given
        List<UserCoupon> userCoupons = List.of(
            UserCoupon.of(1L, 10L, 100L),
            UserCoupon.of(2L, 10L, 200L)
        );

        when(userCouponRepository.findByUserIdAndUsable(anyLong()))
            .thenReturn(userCoupons);

        // when
        Coupons coupons = userCouponService.getUserCoupons(10L);

        // then
        assertThat(coupons.getCoupons()).hasSize(2)
            .extracting("userCouponId")
            .containsExactlyInAnyOrder(1L, 2L);

        assertThat(coupons.getCoupons()).hasSize(2)
            .extracting("couponId")
            .containsExactlyInAnyOrder(100L, 200L);
    }
}