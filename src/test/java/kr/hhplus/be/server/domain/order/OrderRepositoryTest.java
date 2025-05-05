package kr.hhplus.be.server.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문을 저장한다")
    void saveOrder() {

        // given
        Order order = Order.create(1L, 10L, 100, List.of(
            OrderItem.create(100L, "productA", 1_000L, 1)
        ));

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getUserId()).isEqualTo(1L);
        assertThat(savedOrder.getUserCouponId()).isEqualTo(10L);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(900L);
        assertThat(savedOrder.getOrderItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 ID가 유효하지 않으면 주문을 조회할 수 없다")
    void getOrderWithInvalidId() {

        // when, then
        assertThatThrownBy(() -> orderRepository.findById(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 ID로 주문을 찾는다")
    void getOrderWithValidId() {

        // given
        Order order = Order.create(1L, 10L, 1_000L, List.of(
            OrderItem.create(100L, "productA", 1_000L, 10)
        ));

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(savedOrder.getOrderItems()).hasSize(1);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(9_000L);
    }

    @Test
    @DisplayName("주문 ID 리스트로 주문 상품을 찾는다")
    void getOrderByOrderIds() {

        // given
        Order order1 = Order.create(1L, 10L, 1000, List.of(
            OrderItem.create(100L, "productA", 1_000L, 10)
        ));
        Order order2 = Order.create(2L, 10L, 1000, List.of(
            OrderItem.create(100L, "productA", 1_000L, 10)
        ));

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Long> ids = List.of(order1.getId(), order2.getId());

        // when
        List<OrderItem> orderItems = orderRepository.findOrderIdsIn(ids);

        // then
        assertThat(orderItems).hasSize(2)
            .extracting(OrderItem::getOrder)
            .containsExactlyInAnyOrder(order1, order2);
    }
}
