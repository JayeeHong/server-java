package kr.hhplus.be.server.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.application.order.OrderInfoEventPublisher;
import kr.hhplus.be.server.domain.order.OrderCommand.Create;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderInfoEventPublisher eventPublisher;

    @Test
    @DisplayName("주문을 생성한다")
    void createOrder() {

        // given
        Create command = Create.of(1L, 10L, 1_000L, List.of(
            OrderCommand.OrderItem.of(100L, "productA", 1_000L, 10))
        );

        // when
        OrderInfo.Order order = orderService.createOrder(command);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(9_000L);
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문이 존재하지 않는다면 결제할 수 없다")
    void paidOrderWithInvalidOrder() {

        // given
        when(orderRepository.findById(any()))
            .thenThrow(new IllegalArgumentException("주문이 존재하지 않습니다."));

        // when, then
        assertThatThrownBy(() -> orderService.paidOrder(any()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 결제한다")
    void paidOrder() {

        // given
        Order order = Order.of(100L, 1L, 10L, 1_000L, List.of(
            OrderItem.create(100L, "productA", 1_000L, 10)
        ));

        when(orderRepository.findById(order.getId())).thenReturn(order);

        // when
        orderService.paidOrder(order.getId());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAY_COMPLETE);
        verify(eventPublisher).success(argThat(event -> event.getOrderId().equals(order.getId())));
    }
}
