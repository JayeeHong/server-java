package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Slf4j
public class ProductCacheTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 정보 캐시 적용 테스트")
    @Commit
    @Sql(scripts = "/sql/productCache.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void cacheProductTest() {

        Product productA = Product.create("productA", 1000, 100);
        productRepository.save(productA);
        Product productB = Product.create("productB", 1000, 100);
        productRepository.save(productB);

        long findStart = System.currentTimeMillis();
        Product findProduct = productService.getProductInfo(productA.getId());
        long findEnd = System.currentTimeMillis();
        log.info("findEnd - findStart: {}", findEnd - findStart);

        long findStartCache = System.currentTimeMillis();
        Product findProductCache = productService.getProductInfoCache(productB.getId());
        long findEndCache = System.currentTimeMillis();
        log.info("findEndCache - findStartCache: {}", findEndCache - findStartCache);

        assertThat(findProduct.getId()).isEqualTo(productA.getId());
        assertThat(findProductCache.getId()).isEqualTo(productB.getId());

        long decreaseStart = System.currentTimeMillis();
        Product updatedProduct = productService.decreaseStockWithRedisson(findProduct.getId(), 10);
        long decreaseEnd = System.currentTimeMillis();
        log.info("decreaseEnd - decreaseStart: {}", decreaseEnd - decreaseStart);

        long decreaseStartCache = System.currentTimeMillis();
        Product updatedProductCache = productService.decreaseStockWithRedissonCache(findProductCache.getId(), 10);
        long decreaseEndCache = System.currentTimeMillis();
        log.info("decreaseEndCache - decreaseStartCache: {}", decreaseEndCache - decreaseStartCache);

        assertThat(updatedProduct.getStock()).isEqualTo(90);
        assertThat(updatedProductCache.getStock()).isEqualTo(90);

        Long productAId = findProduct.getId();
        Long productBId = findProductCache.getId();

        long find2Start = System.currentTimeMillis();
        Product find2Product = productService.getProductInfo(productAId);
        long find2End = System.currentTimeMillis();
        log.info("find2End - find2Start: {}", find2End - find2Start);

        long find2StartCache = System.currentTimeMillis();
        Product find2ProductCache = productService.getProductInfo(productBId);
        long find2EndCache = System.currentTimeMillis();
        log.info("find2EndCache - find2StartCache: {}", find2EndCache - find2StartCache);

        long deleteStart = System.currentTimeMillis();
        productService.deleteProduct(productAId);
        long deleteEnd = System.currentTimeMillis();
        log.info("deleteEnd - deleteStart: {}", deleteEnd - deleteStart);

        long deleteStartCache = System.currentTimeMillis();
        productService.deleteProduct(productBId);
        long deleteEndCache = System.currentTimeMillis();
        log.info("deleteEndCache - deleteStartCache: {}", deleteEndCache - deleteStartCache);

        long find3Start = System.currentTimeMillis();
        Product find3Product = productService.getProductInfo(productAId);
        long find3End = System.currentTimeMillis();
        log.info("find3End - find3Start: {}", find3End - find3Start);

        long find3StartCache = System.currentTimeMillis();
        Product find3ProductCache = productService.getProductInfo(productBId);
        long find3EndCache = System.currentTimeMillis();
        log.info("find3EndCache - find3StartCache: {}", find3EndCache - find3StartCache);

        assertThat(find3Product).isNull();
        assertThat(find3ProductCache).isNull();
    }

}
