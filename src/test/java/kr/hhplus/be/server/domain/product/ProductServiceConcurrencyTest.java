package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("동일 상품 주문이 동시에 발생하면 재고가 마이너스로 내려갈 수 있다")
    void decreaseStockConcurrencyTest() throws InterruptedException {

        Product product = Product.create("productAA", 1_000L, 10, ProductStatus.SELLING);
        productRepository.save(product);

        Product findProduct = productRepository.findById(product.getId());

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ProductCommand.OrderItem command = ProductCommand.OrderItem.of(findProduct.getId(), 3);
                    productService.decreaseStock(command);
                } catch (Exception e) {
                    System.out.println("재고 감소 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        int quantity = productRepository.findById(product.getId()).getQuantity();
        System.out.println("남은 재고: " + quantity);
        assertThat(quantity).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("상품 재고 감소 Redisson 테스트 - 성공")
    void decreaseStockRedissonSuccessTest() throws InterruptedException {

        Product product = Product.create("productAA", 1_000L, 10, ProductStatus.SELLING);
        productRepository.save(product);

        Product findProduct = productRepository.findById(product.getId());

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ProductCommand.OrderItem command = ProductCommand.OrderItem.of(findProduct.getId(), 1);
                    productService.decreaseStockWithRedisson(command);
                } catch (Exception e) {
                    System.out.println("재고 감소 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        int quantity = productRepository.findById(product.getId()).getQuantity();
        System.out.println("남은 재고: " + quantity);
        assertThat(quantity).isZero();
    }
}
