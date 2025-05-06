package kr.hhplus.be.server.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
class CouponServiceConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 선착순 발급 시 모든 요청에 대해 발급한다")
    void issueCouponConcurrencyTest() throws InterruptedException {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 100, LocalDateTime.of(9999, 12, 31, 23, 59));
        coupon.publish();
        couponRepository.save(coupon);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(coupon.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("쿠폰 발급 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(3);
        assertThat(failCount.get()).isZero();

        Coupon findCoupon = couponRepository.findById(coupon.getId());
        assertThat(findCoupon.getQuantity()).isEqualTo(97);
    }

    @Test
    @DisplayName("쿠폰 선착순 발급 시 수량이 부족하면 예외가 발생한다")
    void issueCouponConcurrencyTest2() throws InterruptedException {

        // given
        Coupon coupon = Coupon.create("couponA", 1000, 3, LocalDateTime.of(9999, 12, 31, 23, 59));
        coupon.publish();
        couponRepository.save(coupon);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(coupon.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("쿠폰 발급 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertThat(successCount.get()).isEqualTo(3);
        assertThat(failCount.get()).isEqualTo(2);

        Coupon findCoupon = couponRepository.findById(coupon.getId());
        assertThat(findCoupon.getQuantity()).isEqualTo(0);
    }
}
