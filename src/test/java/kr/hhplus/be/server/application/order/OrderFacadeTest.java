package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.config.redis.RedissonLockService;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderFacadeTest {

    ProductService productService = mock(ProductService.class);
    CouponService couponService = mock(CouponService.class);
    UserService userService = mock(UserService.class);
    OrderService orderService = mock(OrderService.class);
    RedissonLockService lockService = mock(RedissonLockService.class);

    OrderFacade orderFacade = new OrderFacade(productService, couponService, userService,
        orderService, lockService);

    @Test
    @DisplayName("주문 흐름 성공")
    void placeOrder_success() {
        // given
        OrderRequest.Command request = new OrderRequest.Command(
            1L,
            null,
            List.of(new OrderRequest.Item(101L, 2)) // 2개 주문
        );

        // 상품 가격 10,000원 * 2개 = 20,000원
        Product product = Product.of(101L, "상품1", 10000, 98);
        when(productService.getAndDecreaseStock(any())).thenReturn(List.of(product));

        Order order = Order.of(null, 1L, 20000);
        order.addItem(OrderItem.of(null, 101L, 2));

        Order saved = Order.of(1001L, 1L, 20000); // 저장 후 반환되는 주문 객체
        when(orderService.createOrder(any())).thenReturn(saved);

        // when
        Result result = orderFacade.placeOrder(request);

        // then
        assertEquals(1001L, result.getOrderId());
        assertEquals(20000, result.getTotalAmount());
    }


    @Test
    @DisplayName("상품 재고 없음 등으로 실패 시 예외 발생")
    void placeOrder_stockFail() {
        OrderRequest.Command request = new OrderRequest.Command(1L, null, List.of(
            new OrderRequest.Item(999L, 999)
        ));

        when(productService.getAndDecreaseStock(any()))
            .thenThrow(new IllegalStateException("재고가 부족합니다."));

        assertThrows(IllegalStateException.class, () -> {
            orderFacade.placeOrder(request);
        });
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않으면 주문 실패")
    void placeOrder_invalidCoupon() {
        OrderRequest.Command request = new OrderRequest.Command(1L, 99L, List.of(
            new OrderRequest.Item(101L, 1)
        ));

        when(productService.getAndDecreaseStock(any())).thenReturn(List.of(
            Product.of(101L, "상품1", 10000, 99)
        ));
        when(productService.getPrice(101L)).thenReturn(10000);
        doThrow(new IllegalArgumentException("유효하지 않은 쿠폰입니다."))
            .when(couponService).useCoupon(1L, 99L);

        assertThrows(IllegalArgumentException.class, () -> orderFacade.placeOrder(request));
    }

    @Test
    @DisplayName("사용자 잔액이 부족하면 주문 실패")
    void placeOrder_insufficientBalance() {
        OrderRequest.Command request = new OrderRequest.Command(1L, null, List.of(
            new OrderRequest.Item(101L, 1)
        ));

        when(productService.getAndDecreaseStock(any())).thenReturn(List.of(
            Product.of(101L, "상품1", 10000, 99)
        ));
        when(productService.getPrice(101L)).thenReturn(10000);
        doThrow(new IllegalStateException("잔액이 부족합니다."))
            .when(userService).validateAndPay(1L, 10000);

        assertThrows(IllegalStateException.class, () -> orderFacade.placeOrder(request));
    }

    @Test
    @DisplayName("존재하지 않는 사용자일 경우 주문 실패")
    void placeOrder_invalidUser() {
        OrderRequest.Command request = new OrderRequest.Command(999L, null, List.of(
            new OrderRequest.Item(101L, 1)
        ));

        when(productService.getAndDecreaseStock(any())).thenReturn(List.of(
            Product.of(101L, "상품1", 10000, 99)
        ));
        when(productService.getPrice(101L)).thenReturn(10000);
        doThrow(new IllegalArgumentException("유효하지 않은 사용자입니다."))
            .when(userService).validateAndPay(999L, 10000);

        assertThrows(IllegalArgumentException.class, () -> orderFacade.placeOrder(request));
    }
}
