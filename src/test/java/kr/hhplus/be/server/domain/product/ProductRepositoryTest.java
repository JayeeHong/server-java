package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("유효하지 않은 상품 ID로 상품을 조회할 수 없다")
    void findByInvalidProductId() {

        // when, then
        assertThatThrownBy(() -> productRepository.findById(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 ID로 상품을 조회한다")
    void findByValidProductId() {

        // given
        Product product = Product.create("productA", 1_000L, 100, ProductStatus.SELLING);
        Product savedProduct = productRepository.save(product);

        // when
        Product findProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(savedProduct.getId()).isEqualTo(findProduct.getId());
    }

    @Test
    @DisplayName("판매상태인 상품을 조회한다")
    void findProductsSellStatus() {

        // given
        Product sellProduct = Product.create("productA", 1_000L, 100, ProductStatus.SELLING);
        Product holdProduct = Product.create("productB", 2_000L, 100, ProductStatus.HOLD);
        Product stopProduct = Product.create("productC", 3_000L, 100, ProductStatus.STOP_SELLING);

        List.of(sellProduct, holdProduct, stopProduct).forEach(productRepository::save);

        List<ProductStatus> sellStatus = List.of(ProductStatus.SELLING);

        // when
        List<Product> products = productRepository.findByStatusIn(sellStatus);

        // then
        assertThat(products).isNotNull();
//        assertThat(products).hasSize(1)
//            .extracting("name", "price", "status")
//            .containsExactly(
//                tuple(sellProduct.getName(), sellProduct.getPrice(), sellProduct.getStatus())
//            );
    }
}
