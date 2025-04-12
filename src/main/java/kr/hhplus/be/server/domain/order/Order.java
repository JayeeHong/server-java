package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Long userId;
    private final int totalAmount;
    private final OrderStatus orderStatus;
    private final List<OrderItem> orderItems;
    private final LocalDateTime orderedAt;

    private Order(Long id, Long userId, int totalAmount, List<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = OrderStatus.WAIT;
        this.orderItems = orderItems;
        this.orderedAt = LocalDateTime.now();
    }

    public static Order of(Long id, Long userId, int totalAmount, List<OrderItem> orderItems) {
        return new Order(id, userId, totalAmount, orderItems);
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public int totalAmount() {
        return totalAmount;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public List<OrderItem> orderItems() {
        return orderItems;
    }

    public LocalDateTime orderedAt() {
        return orderedAt;
    }
}
