package kr.hhplus.be.server.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

@ActiveProfiles("test")
@SpringBootTest
@Slf4j
public class ProductSerivceOptimisticLockTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("상품 재고 감소 낙관적락 테스트")
    void decreaseStockOptimisticLockTest() throws InterruptedException {

        // given
        Product product = Product.create("productA", 1000, 10);
        productRepository.save(product);

        // when
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger catchCount = new AtomicInteger();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseStock(product.getId(), 1);
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
        System.out.println("최종 재고: " + updated.getStock());
        assertEquals(updated.getStock(), 8);
        assertEquals(catchCount.get(), 0);
    }

    @Test
    @DisplayName("상품 재고 감소 낙관적락 실패 테스트 - 재시도 처리하지 않음")
    void decreaseStockOptimisticLockFailTest() throws InterruptedException {

        // given
        Product product = Product.create("productA", 1000, 10);
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
        System.out.println("최종 재고: " + updated.getStock());
        assertEquals(updated.getStock(), 9);
        assertEquals(catchCount.get(), 1);
    }

}
