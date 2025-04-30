package kr.hhplus.be.server.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시 상품은 필수이다")
    void createWithoutOrderItems() {
        // when, then
        assertThatThrownBy(() -> Order.create(1L, 1L, 1000, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품이 비어 있는 주문은 생성할 수 없다")
    void createWithEmptyOrderItems() {
        // when, then
        assertThatThrownBy(() -> Order.create(1L, 1L, 1000, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("할인이 없는 주문을 생성한다")
    void createWithoutDiscount() {

        // given
        List<OrderItem> orderItems = List.of(
            OrderItem.create(1L, "orderItemA", 1_000L, 1),
            OrderItem.create(2L, "orderItemB", 2_000L, 1),
            OrderItem.create(3L, "orderItemC", 3_000L, 1),
            OrderItem.create(4L, "orderItemD", 4_000L, 1)
        );

        // when
        Order order = Order.create(1L, null, 0, orderItems);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("할인이 있는 주문을 생성한다")
    void createWithDiscount() {

        // given
        List<OrderItem> orderItems = List.of(
            OrderItem.create(1L, "orderItemA", 1_000L, 1),
            OrderItem.create(2L, "orderItemB", 2_000L, 1),
            OrderItem.create(3L, "orderItemC", 3_000L, 1),
            OrderItem.create(4L, "orderItemD", 4_000L, 1)
        );

        // when
        Order order = Order.create(1L, null, 1_000L, orderItems);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(10_000L - 1_000L);
    }
}
