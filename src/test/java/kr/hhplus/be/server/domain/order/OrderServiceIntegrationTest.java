package kr.hhplus.be.server.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

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
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다")
    void createOrder() {

        // given
        OrderCommand.Create command = OrderCommand.Create.of(1L, 10L, 1_000L, List.of(
            OrderCommand.OrderItem.of(100L, "productA", 1_000L, 10),
            OrderCommand.OrderItem.of(200L, "productB", 2_000L, 20)
        ));

        // when
        OrderInfo.Order order = orderService.createOrder(command);

        // then
        assertThat(order.getOrderId()).isNotNull();
        assertThat(order.getTotalPrice()).isEqualTo(49_000L);
        assertThat(order.getDiscountPrice()).isEqualTo(1_000L);
    }

    @Test
    @DisplayName("주문 생성 및 주문의 결제를 처리한다")
    void paidOrder() {

        // given
        OrderCommand.Create command = OrderCommand.Create.of(1L, 10L, 1_000L, List.of(
            OrderCommand.OrderItem.of(100L, "productA", 1_000L, 10),
            OrderCommand.OrderItem.of(200L, "productB", 2_000L, 20)
        ));

        OrderInfo.Order order = orderService.createOrder(command);

        // when
        orderService.paidOrder(order.getOrderId());

        // then
        OrderInfo.Order findOrder = orderService.getOrder(order.getOrderId());
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.PAY_COMPLETE);
    }
}
