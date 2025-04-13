package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.PopularProduct;
import kr.hhplus.be.server.domain.product.ProductStatRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductStatServiceTest {

    ProductStatRepository productStatRepository = mock(ProductStatRepository.class);
    ProductStatService productStatService = new ProductStatService(productStatRepository);

    @Test
    @DisplayName("최근 3일간 인기 상품 5개를 조회할 수 있다")
    void getPopularProducts_success() {
        // given
        List<PopularProduct> topProducts = List.of(
            new PopularProduct(1L, "아메리카노", 100),
            new PopularProduct(2L, "카페라떼", 85)
        );

        when(productStatRepository.findTop5PopularProducts()).thenReturn(topProducts);

        // when
        List<ProductResponse.HotProduct> result = productStatService.getPopularProducts();

        // then
        assertEquals(2, result.size());
        assertEquals("아메리카노", result.get(0).productName());
        assertEquals(100, result.get(0).totalSold());
        verify(productStatRepository, times(1)).findTop5PopularProducts();
    }

    @Test
    @DisplayName("인기 상품이 없을 경우 빈 리스트를 반환한다")
    void getPopularProducts_emptyList() {
        // given
        when(productStatRepository.findTop5PopularProducts()).thenReturn(List.of());

        // when
        List<ProductResponse.HotProduct> result = productStatService.getPopularProducts();

        // then
        assertNotNull(result);           // 리스트는 null이 아님
        assertTrue(result.isEmpty());    // 비어 있어야 함
        verify(productStatRepository, times(1)).findTop5PopularProducts();
    }

}
