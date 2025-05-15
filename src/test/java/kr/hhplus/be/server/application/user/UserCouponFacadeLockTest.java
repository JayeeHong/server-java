package kr.hhplus.be.server.application.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.support.ConcurrencyTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

public class UserCouponFacadeLockTest extends ConcurrencyTestSupport {

    @Autowired
    private UserCouponFacade userCouponFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;
//
//    @Autowired
//    private RedisCacheCleaner redisCacheCleaner;
//
//    @AfterEach
//    void tearDown() {
//        redisCacheCleaner.clean();
//    }

    @Test
    @DisplayName("분산락 - 동시에 선착순 쿠폰 발급 시 모든 요청에 대해 발급되어야 한다")
    void issueCouponWithDistributedLock() {

        // given
        User user1 = User.create("userA");
        userRepository.save(user1);

        User user2 = User.create("userB");
        userRepository.save(user2);

        Coupon coupon = Coupon.create("couponA", 1000, 5, LocalDateTime.now().plusDays(1));
        coupon.publish();
        couponRepository.save(coupon);

        UserCouponCriteria.Publish criteria1 = UserCouponCriteria.Publish.of(user1.getId(), coupon.getId());
        UserCouponCriteria.Publish criteria2 = UserCouponCriteria.Publish.of(user2.getId(), coupon.getId());

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        executeConcurrency(List.of(
            () -> {
                try {
                    userCouponFacade.issueUserCoupon(criteria1);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            },
            () -> {
                try {
                    userCouponFacade.issueUserCoupon(criteria2);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            }
        ));

        // then
//        assertThat(successCount.get()).isEqualTo(1);
//        assertThat(failCount.get()).isZero();

        Coupon remainCoupon = couponRepository.findById(coupon.getId());
//        assertThat(remainCoupon.getQuantity()).isEqualTo(3);
    }

}
