package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderItem;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderItems;
import kr.hhplus.be.server.domain.product.ProductInfo.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("유효하지 않은 ID로 주문 상품 목록을 조회할 수 없다")
    void getOrderProductInvalidId() {

        // given
        OrderItems command = mock(OrderItems.class);
        OrderItem orderItemCommand = mock(OrderItem.class);

        when(command.getOrderItems()).thenReturn(List.of(orderItemCommand));
        when(productRepository.findById(anyLong())).thenThrow(new IllegalArgumentException());

        // when, then
        assertThatThrownBy(() -> productService.getOrderProducts(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 가능한 상품 목록을 조회 상품 상태가 판매 중이 아닌 상품이 있으면 예외가 발생한다")
    void getSellingStatusOrderProduct() {

        // given
        OrderItems command = mock(OrderItems.class);
        OrderItem orderItem = mock(OrderItem.class);

        when(command.getOrderItems()).thenReturn(List.of(orderItem, orderItem));
        when(productRepository.findById(anyLong())).thenReturn(
            Product.create("productA", 1_000L, 100, ProductStatus.STOP_SELLING));

        // when, then
        assertThatThrownBy(() -> productService.getOrderProducts(command))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("주문 상품 목록을 조회한다")
    void getOrderProducts() {
        // given
        OrderItems command = mock(OrderItems.class);
        OrderItem orderItem = mock(OrderItem.class);

        when(orderItem.getQuantity()).thenReturn(100);
        when(command.getOrderItems()).thenReturn(List.of(orderItem, orderItem));

        when(productRepository.findById(anyLong())).thenReturn(
            Product.create("productA", 1_000L, 100, ProductStatus.SELLING));

        // when
        ProductInfo.OrderItems orderItems = productService.getOrderProducts(command);

        // then
        assertThat(orderItems.getOrderItems()).hasSize(2)
            .extracting("productName", "productPrice", "quantity")
            .containsExactly(
                tuple("productA", 1_000L, 100),
                tuple("productA", 1_000L, 100)
            );
    }

    @Test
    @DisplayName("판매 가능한 상품을 조회한다")
    void getSellStatusProduct() {

        // given
        List<Product> products = List.of(
            Product.create("productA", 1_000L, 100, ProductStatus.SELLING)
        );

        when(productRepository.findByStatusIn(anyList())).thenReturn(products);

        // when
        Products sellingProducts = productService.getSellingProducts();

        // then
        assertThat(sellingProducts.getProducts()).hasSize(1);
    }
}
