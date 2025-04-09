package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.domain.common.Price;
import kr.hhplus.be.server.domain.common.Stock;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품상세_조회시_존재하지_않는_상품_예외처리() {
        // given
        given(productRepository.findById(1L)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> productService.getProduct(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 상품입니다.");
    }

    @Test
    void 상품_재입고_정상처리() {
        // given
        long productId = 1L;
        int restockAmount = 10;

        Product product = mock(Product.class);
        Product updatedProduct = mock(Product.class);
        ProductResponse.Product responseDto = new ProductResponse.Product(1L, "노트북", 1000000, 15);

        when(productRepository.findById(productId)).thenReturn(product);

        // 스텁 설정: restock 후 내부 재고는 15개라고 가정
        doAnswer(invocation -> {
            when(product.getStock()).thenReturn(new Stock(15));
            return null;
        }).when(product).restock(restockAmount);

        when(productRepository.updateStock(productId, 15)).thenReturn(updatedProduct);
        when(updatedProduct.translateProduct()).thenReturn(responseDto);

        // when
        ProductResponse.Product result = productService.restockProduct(productId, restockAmount);

        // then
        assertNotNull(result);
        assertEquals(15, result.stock());
        assertEquals("노트북", result.name());
        verify(productRepository).findById(productId);
        verify(product).restock(restockAmount);
        verify(productRepository).updateStock(productId, 15);
    }

    @Test
    void 상품_재입고시_존재하지_않는_상품_예외처리() {
        // given
        long invalidProductId = 99L;

        when(productRepository.findById(invalidProductId)).thenReturn(null);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            productService.restockProduct(invalidProductId, 10));

        assertEquals("유효하지 않은 상품입니다.", ex.getMessage());
        verify(productRepository).findById(invalidProductId);
        verify(productRepository, never()).updateStock(anyLong(), anyInt());
    }

    @Test
    void 상품_구매_정상처리() {
        // given
        long productId = 1L;
        int purchaseStock = 5;

        Product product = mock(Product.class);
        Product updatedProduct = mock(Product.class);
        ProductResponse.Product responseDto = new ProductResponse.Product(1L, "노트북", 1000000, 10);

        when(productRepository.findById(productId)).thenReturn(product);

        // 미리 getStock()은 stub 해놓고
        when(product.getStock()).thenReturn(new Stock(10));
        // purchase()는 void니까 doNothing()으로 처리
        doNothing().when(product).purchase(purchaseStock);

        when(productRepository.updateStock(productId, 10)).thenReturn(updatedProduct);
        when(updatedProduct.translateProduct()).thenReturn(responseDto);

        // when
        ProductResponse.Product result = productService.purchaseProduct(productId, purchaseStock);

        // then
        assertNotNull(result);
        assertEquals(10, result.stock());
        assertEquals("노트북", result.name());
        verify(productRepository).findById(productId);
        verify(product).purchase(purchaseStock);
        verify(productRepository).updateStock(productId, 10);
    }

    @Test
    void 상품_구매시_존재하지_않는_상품_예외처리() {
        // given
        long invalidProductId = 99L;

        when(productRepository.findById(invalidProductId)).thenReturn(null);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            productService.purchaseProduct(invalidProductId, 10));

        assertEquals("유효하지 않은 상품입니다.", ex.getMessage());
        verify(productRepository).findById(invalidProductId);
        verify(productRepository, never()).updateStock(anyLong(), anyInt());
    }

    @Test
    void 상품_가격_수정_정상처리() {
        // given
        long productId = 1L;
        int newPriceValue = 1200000;

        Product product = mock(Product.class); // 변경 대상
        Product updatedProduct = mock(Product.class); // 업데이트 후 반환
        ProductResponse.Product responseDto = new ProductResponse.Product(1L, "노트북", newPriceValue, 5);

        when(productRepository.findById(productId)).thenReturn(product);

        // 기존 재고 수량이 5라고 가정
        when(product.getStock()).thenReturn(new Stock(5));

        // 가격 변경 메서드는 void
        doNothing().when(product).changePrice(new Price(newPriceValue));

        when(productRepository.updateStock(productId, 5)).thenReturn(updatedProduct);
        when(updatedProduct.translateProduct()).thenReturn(responseDto);

        // when
        ProductResponse.Product result = productService.changeProductPrice(productId, newPriceValue);

        // then
        assertNotNull(result);
        assertEquals("노트북", result.name());
        assertEquals(newPriceValue, result.price());
        assertEquals(5, result.stock());

        verify(productRepository).findById(productId);
        verify(product).changePrice(new Price(newPriceValue));
        verify(productRepository).updateStock(productId, 5);
    }

    @Test
    void 상품_가격_수정시_존재하지_않는_상품_예외처리() {
        // given
        long invalidProductId = 99L;

        when(productRepository.findById(invalidProductId)).thenReturn(null);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            productService.changeProductPrice(invalidProductId, 10));

        assertEquals("유효하지 않은 상품입니다.", ex.getMessage());
        verify(productRepository).findById(invalidProductId);
        verify(productRepository, never()).updateStock(anyLong(), anyInt());
    }

}