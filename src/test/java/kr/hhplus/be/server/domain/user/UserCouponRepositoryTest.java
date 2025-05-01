package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class UserCouponRepositoryTest {

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    @DisplayName("사용자 쿠폰을 저장한다")
    void save() {

        // given
        UserCoupon userCoupon = UserCoupon.create(1L, 100L);

        // when
        userCouponRepository.save(userCoupon);

        // then
        assertThat(userCoupon.getId()).isNotNull();
    }

    @Test
    @DisplayName("사용자가 보유한 쿠폰이 없으면 조회할 쿠폰이 없다")
    void findByUserIdAndCouponIdIsEmpty() {

        // when
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(1L, 1L);

        // then
        assertThat(userCoupon).isNull();
    }

    @Test
    @DisplayName("사용자가 쿠폰을 발급받은 적이 없다면 조회할 쿠폰이 없다")
    void findByIdIsEmpty() {

        // when, then
        assertThatThrownBy(() -> userCouponRepository.findById(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자가 보유한 쿠폰을 조회한다")
    void findByUserIdAndCouponId() {

        // given
        UserCoupon userCoupon = UserCoupon.create(1L, 100L);
        userCouponRepository.save(userCoupon);

        // when
        UserCoupon findUserCoupon = userCouponRepository.findByUserIdAndCouponId(1L, 100L);

        // then
        assertThat(findUserCoupon.getId()).isNotNull();
        assertThat(findUserCoupon.getUserId()).isEqualTo(1L);
        assertThat(findUserCoupon.getCouponId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("사용자 쿠폰 ID로 사용자가 보유한 쿠폰을 조회한다")
    void findById() {

        // given
        UserCoupon userCoupon = UserCoupon.create(1L, 100L);
        userCouponRepository.save(userCoupon);

        // when
        UserCoupon findCoupon = userCouponRepository.findById(userCoupon.getId());

        // then
        assertThat(findCoupon.getId()).isEqualTo(userCoupon.getId());
        assertThat(findCoupon.getUserId()).isEqualTo(userCoupon.getUserId());
        assertThat(findCoupon.getCouponId()).isEqualTo(userCoupon.getCouponId());
    }

    @Test
    @DisplayName("사용자가 보유한 사용할 수 있는 쿠폰을 조회한다")
    void getCanUseUserCoupons() {

        // given
        Long userId = 1L;
        Long anotherUserId = 100L;

        // 사용하지 않은 쿠폰
        UserCoupon userCoupon = UserCoupon.create(userId, 100L);
        userCouponRepository.save(userCoupon);

        // 사용한 쿠폰
        UserCoupon usedUserCoupon = UserCoupon.create(userId, 200L);
        usedUserCoupon.use();
        userCouponRepository.save(usedUserCoupon);

        // 다른 사용자의 쿠폰
        UserCoupon anotherUserCoupon = UserCoupon.create(anotherUserId, 100L);
        userCouponRepository.save(anotherUserCoupon);

        // when
        List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndUsable(userId);

        // then
        assertThat(userCoupons).hasSize(1)
            .extracting("userId", "couponId", "usedAt")
            .containsExactlyInAnyOrder(
                tuple(userCoupon.getUserId(), userCoupon.getCouponId(), userCoupon.getUsedAt())
            );
    }
}
