package kr.hhplus.be.server.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductInfo.Products;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductFacadeUnitTest {

    @InjectMocks
    private ProductFacade productFacade;

    @Mock
    private ProductService productService;

    @Test
    @DisplayName("판매 가능한 상품 목록을 조회한다")
    void getProducts() {

        // given
        ProductInfo.Products products = mock(ProductInfo.Products.class);

        when(products.getProducts())
            .thenReturn(
                List.of(
                    ProductInfo.Product.of(1L, "productA", 1_000L, 10),
                    ProductInfo.Product.of(2L, "productB", 2_000L, 20)
                )
            );

        when(productService.getSellingProducts())
            .thenReturn(products);

        // when
        ProductResult.Products findProducts = productFacade.getProducts();

        // then
        InOrder inOrder = inOrder(productService);
        inOrder.verify(productService, times(1)).getSellingProducts();

        assertThat(findProducts.getProducts()).hasSize(2)
            .extracting("productId", "quantity")
            .containsExactlyInAnyOrder(
                tuple(1L, 10),
                tuple(2L, 20)
            );
    }
}