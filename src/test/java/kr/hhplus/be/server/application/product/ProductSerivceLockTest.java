package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.config.redis.RedissonLockManager;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
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
public class ProductSerivceLockTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedissonLockManager lockService;

    @Test
    @DisplayName("상품 재고 감소 Redisson 테스트 - 성공")
    void decreaseStockRedissonSuccessTest() {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);
        productRepository.save(product);

        // when
        productService.decreaseStockWithRedisson(product.getId(), 1);

        // then
        Product updatedProduct = productRepository.findById(product.getId());
        assertThat(updatedProduct.getQuantity()).isEqualTo(9);
    }

    @Test
    @DisplayName("상품 재고 감소 비관적락 테스트")
    void decreaseStockPessimisticLockTest() throws InterruptedException {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);
        productRepository.save(product);

        // when
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseStockWithPessimisticLock(product.getId(), 1);
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
        Product updated = productRepository.findById(product.getId());
        System.out.println("최종 재고: " + updated.getQuantity());
        assertEquals(updated.getQuantity(), 8);
        assertEquals(catchCount.get(), 0);
    }

    @Test
    @DisplayName("상품 재고 감소 낙관적락 테스트")
    void decreaseStockOptimisticLockTest() throws InterruptedException {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);
        productRepository.save(product);

        // when
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseStockWithOptimisticLock(product.getId(), 1);
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
        Product updated = productRepository.findById(product.getId());
        System.out.println("최종 재고: " + updated.getQuantity());
        assertEquals(updated.getQuantity(), 8);
        assertEquals(catchCount.get(), 0);
    }

    @Test
    @DisplayName("상품 재고 감소 낙관적락 실패 테스트 - 재시도 처리하지 않음")
    void decreaseStockOptimisticLockFailTest() throws InterruptedException {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);
        productRepository.save(product);

        // when
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseStockWithoutConcurrency(product.getId(), 1);
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
        Product updated = productRepository.findById(product.getId());
        System.out.println("최종 재고: " + updated.getQuantity());
        assertEquals(updated.getQuantity(), 9);
        assertEquals(catchCount.get(), 1);
    }

}
