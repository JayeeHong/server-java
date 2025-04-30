package kr.hhplus.be.server.integration.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.config.redis.RedissonLockManager;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Command;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Item;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
@Disabled
public class OrderIntegrationLockTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private RedissonLockManager lockService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("주문 등록 Redisson 락 테스트 - 정상 주문 처리")
    void placeOrderWithLockSuccessTest() {

        // given
        // 1. 상품 등록
        Product product = Product.create("productA", 10000, 100, ProductStatus.SELLING);
        productRepository.save(product);

        // 2. 사용자 등록
        User user = User.create("userA");
        userRepository.save(user);
        // 2-1. 사용자 잔액 충전
        Balance balance = Balance.create(user.getId(), 10000);
        balanceRepository.save(balance);

        // 3. 주문 요청 생성
        List<Item> items = List.of(new Item(product.getId(), 2)); // 상품 2개 주문
        Command command = Command.of(user.getId(), null, items);

        // when
        Result result = orderFacade.placeOrderWithLock(command);

        // then
        // 주문 검증
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotalAmount()).isEqualTo(20000);
        assertThat(result.getUsedCouponId()).isNull(); // 쿠폰 사용하지 않음

        // 상품, 잔액 검증
        Product findProduct = productRepository.findById(product.getId());
        int userBalance = balanceRepository.getTotalBalance(user.getId());

        assertThat(findProduct.getQuantity()).isEqualTo(98);
        assertThat(userBalance).isEqualTo(980000);
    }

    @Test
    @DisplayName("락 선점 중 주문 실패 테스트")
    void placeOrderWithLockFailTest_dueToLock() throws InterruptedException {
        // given
        // 1. 상품 등록
        Product product = Product.create("productA", 10000, 100, ProductStatus.SELLING);
        productRepository.save(product);

        // 2. 사용자 등록
        User user = User.create("userA");
        userRepository.save(user);
        // 2-1. 사용자 잔액 충전
        Balance balance = Balance.create(user.getId(), 10000);
        balanceRepository.save(balance);

        // 3. 쿠폰 등록
        Coupon coupon = Coupon.of(null, "1000원 할인", 1000, 3, LocalDateTime.now());
        couponRepository.save(coupon);

        // 4. 주문 요청 생성
        List<Item> items = List.of(new Item(product.getId(), 2)); // 상품 2개 주문
        Command command = Command.of(user.getId(), coupon.getId(), items);

        // 락을 미리 잡아놓기 (다른 스레드처럼 시뮬레이션)
        String productLockKey = "product:" + product.getId();
        String couponLockKey = "coupon:" + coupon.getId();

        boolean productLocked = lockService.tryLock(productLockKey);
        assertThat(productLocked).isTrue();

        boolean couponLocked = lockService.tryLock(couponLockKey);
        assertThat(couponLocked).isTrue();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            // when
            executorService.submit(() -> {
                try {
                    assertThatThrownBy(() -> orderFacade.placeOrderWithLock(command))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("현재 주문 처리 중입니다. 잠시 후 다시 시도해주세요.");
                } finally {
                    latch.countDown();
                }
            });

            latch.await(); // 다른 스레드 완료 대기
        } finally {
            lockService.unlock(productLockKey);
            lockService.unlock(couponLockKey);
            executorService.shutdown();
        }
    }
}
