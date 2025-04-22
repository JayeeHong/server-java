package kr.hhplus.be.server.application.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Slf4j
@Disabled
public class CouponServiceSyncFailTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    @DisplayName("한 명의 사용자가 동일한 쿠폰을 여러 개 발급하는 동시성 문제 발생 - 차감 후 쿠폰 갯수 체크")
    void syncIssueOneUserDuplicateCouponCheckCouponCnt() throws InterruptedException {

        // given
        User user = User.create("userA");
        userRepository.save(user);

        Coupon coupon = Coupon.of(null, "1000원 할인", 1000, 2, LocalDateTime.now());
        coupon.issue();
//        couponRepository.save(coupon);

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(user.id(), coupon.getId());
                } catch (Exception e) {
                    // 예외 터지는 갯수 체크
                    catchCount.getAndIncrement();
                    // 예외 로그
                    log.error("{} (error message: {})", e.getClass(), e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        // exception 2번 발생 (3번 발급 중 2번은 Deadlock 발생)
        assertThat(catchCount.get()).isEqualTo(2);

        // 쿠폰 3번 발급했으나 쿠폰 수량은 1개만 차감되어야 함
        Coupon findCoupon = couponRepository.findById(coupon.getId());
        assertThat(findCoupon.getStock()).isEqualTo(1);

        // 쿠폰이 정상적으로 발급되었으면 사용자에게 발급한 쿠폰이 1장이어야 함
        List<UserCoupon> findUserCoupons = userCouponRepository.findAllByUserId(user.id());
        assertThat(findUserCoupons).hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("한 명의 사용자가 동일한 쿠폰을 여러 개 발급하는 동시성 문제 발생 - 사용자에게 발급된 쿠폰 갯수 체크")
    void syncIssueOneUserDuplicateCouponCheckUserCouponCnt() throws InterruptedException {

        // given
        User user = User.create("userA");
        userRepository.save(user);

        Coupon coupon = Coupon.of(null, "1000원 할인", 1000, 2, LocalDateTime.now());
        coupon.issue();

        UserCoupon userCoupon = UserCoupon.issue(user.id(), coupon, LocalDateTime.now());
        userCouponRepository.save(userCoupon);

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    UserCoupon sameUserCoupon = UserCoupon.issue(user.id(), coupon,
                        LocalDateTime.now());
                    userCouponRepository.save(sameUserCoupon);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        // 쿠폰이 정상적으로 발급되었으면 사용자에게 발급한 쿠폰이 1장이어야 함
        List<UserCoupon> findUserCoupons = userCouponRepository.findAllByUserId(user.id());
        assertThat(findUserCoupons).isEqualTo(1);
    }

}
