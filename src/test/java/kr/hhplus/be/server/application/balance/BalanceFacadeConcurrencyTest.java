package kr.hhplus.be.server.application.balance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class BalanceFacadeConcurrencyTest {

    @Autowired
    private BalanceFacade balanceFacade;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("동시에 잔액 감소를 시도할 때 잔액이 부족하면 잔액 감소 처리되면 안된다")
    void chargeBalanceConcurrencyTest() throws InterruptedException {

        User user = User.create("userA");
        userRepository.save(user);

        BalanceCriteria.Charge chargeCriteria = BalanceCriteria.Charge.of(user.getId(), 1_000L);
        balanceFacade.chargeBalance(chargeCriteria);

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    BalanceCriteria.Use useCriteria = BalanceCriteria.Use.of(user.getId(), 500L);
                    balanceFacade.useBalance(useCriteria);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("잔액 사용 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Balance balance = balanceRepository.findOptionalByUserId(user.getId()).orElseThrow();
        System.out.println("최종 잔액 = " + balance.getAmount());
        assertThat(balance.getAmount()).isGreaterThanOrEqualTo(0);
    }

}
