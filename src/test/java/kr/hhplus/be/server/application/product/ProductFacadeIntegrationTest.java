package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.hhplus.be.server.application.product.ProductResult.Products;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductInfo.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductFacadeIntegrationTest {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private ProductService productService;

    ProductInfo.Product product1;
    ProductInfo.Product product2;

    @BeforeEach
    void setUp() {
        ProductCommand.Create command1 = ProductCommand.Create.of("productA", 1_000L, 10, ProductStatus.SELLING);
        ProductCommand.Create command2 = ProductCommand.Create.of("productB", 2_000L, 10, ProductStatus.SELLING);

        product1 = productService.saveProduct(command1);
        product2 = productService.saveProduct(command2);
    }

    @Test
    @DisplayName("판매 가능한 상품 목록을 조회한다")
    void getProducts() {

        // when
        ProductResult.Products products = productFacade.getProducts();

        // then
        assertThat(products.getProducts()).hasSize(2)
            .extracting(ProductResult.Product::getProductId)
            .containsExactlyInAnyOrder(product1.getProductId(), product2.getProductId());
    }
}