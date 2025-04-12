package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    ProductRepository productRepository = mock(ProductRepository.class);
    ProductService productService = new ProductService(productRepository);

    @Test
    @DisplayName("상품 목록 조회가 성공적으로 수행된다")
    void getAllProducts_success() {
        List<Product> products = List.of(
            Product.of(1L, "상품A", 1000, 10),
            Product.of(2L, "상품B", 2000, 20)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse.Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("상품A", result.get(0).getName());
        assertEquals(2000, result.get(1).getPrice());
    }

    @Test
    @DisplayName("상품 목록이 없을 경우 빈 리스트를 반환한다")
    void getAllProducts_emptyResult() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponse.Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
    }
}
