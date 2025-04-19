package kr.hhplus.be.server.integration.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.product.ProductResponse.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.create("productA", 1000, 100);
        productRepository.save(product);
    }

    @Test
    @DisplayName("전체 상품 조회")
    void getAllProductsTest() {

        // when
        List<ProductDto> findAllProducts = productService.getAllProducts();

        // then
        assertThat(findAllProducts.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 재고 감소 성공")
    void getAndDecreaseStockTest() {

        // when
        List<OrderRequest.Item> items = List.of(
            OrderRequest.Item.of(product.getId(), 100));

        List<Product> result = productService.getAndDecreaseStock(items);

        List<ProductDto> findAllProducts = productService.getAllProducts();
        ProductDto productDto = findAllProducts.stream()
            .filter(p -> p.getId().equals(product.getId())).toList().get(0);

        // then
        assertThat(productDto.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품 재고 감소 실패 - 차감하려고 하는 상품의 갯수가 재고보다 많음")
    void getAndDecreaseStockFailTest() {

        // when
        List<OrderRequest.Item> items = List.of(
            OrderRequest.Item.of(product.getId(), 101));

        // then
        assertThatThrownBy(() -> productService.getAndDecreaseStock(items))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("상품 id로 상품 금액 조회")
    void getPriceTest() {

        // when
        int price = productService.getPrice(product.getId());

        // then
        assertThat(price).isEqualTo(1000);
    }

}
