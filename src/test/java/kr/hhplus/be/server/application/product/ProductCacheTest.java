package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductCacheTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 정보 캐시 적용 테스트")
    public void cacheProductTest() {
        Product product = Product.create("productA", 1000, 100, ProductStatus.SELLING);
        productRepository.save(product);

        Product findProduct = productService.getProductInfo(product.getId());
        assertThat(findProduct.getId()).isEqualTo(product.getId());

        Product updatedProduct = productService.decreaseStockWithRedisson(findProduct.getId(), 10);
        assertThat(updatedProduct.getQuantity()).isEqualTo(90);

        Long id = findProduct.getId();
        productService.deleteProduct(id);
        Product deletedProduct = productService.getProductInfo(id);
        assertThat(deletedProduct).isNull();
    }

}
