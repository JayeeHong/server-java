package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserCouponTest {

    @DisplayName("사용한 쿠폰 검증")
    @Test
    void cannotUseTest() {
        // given
        UserCoupon userCoupon = UserCoupon.create(null, null, LocalDateTime.now());

        // when
        userCoupon.use();
        boolean isUsed = userCoupon.cannotUse();

        // then
        assertThat(isUsed).isTrue();
    }

}
