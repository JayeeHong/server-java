package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserCouponTest {

    @Test
    @DisplayName("사용할 수 없는 쿠폰인지 확인한다")
    void cannotUse() {

        // given
        UserCoupon userCoupon = UserCoupon.create(1L, 1L);
        userCoupon.use();

        // when
        boolean isUse = userCoupon.cannotUse();

        // then
        assertThat(userCoupon.getUsedAt()).isNotNull();
    }
}
