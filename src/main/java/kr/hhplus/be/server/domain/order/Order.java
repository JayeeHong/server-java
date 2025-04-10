package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.interfaces.order.OrderItemResponse;
import kr.hhplus.be.server.interfaces.order.OrderResponse;

public class Order {
    private Long id;
    private Long userId;
    private int totalPrice;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private long createdAt;
    private long updatedAt;

    public Order(Long id, long userId, int totalPrice, OrderStatus status, List<OrderItem> orderItems, long createdAt, long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(long userId, int totalPrice, List<OrderItem> orderItems, OrderStatus status) {
        long now = System.currentTimeMillis();
        return new Order(0L, userId, totalPrice, status, orderItems, now, now);
    }

    public OrderResponse.Order toResponse() {
        List<OrderItemResponse.OrderItem> responseItems = orderItems.stream()
            .map(OrderItem::toResponse)
            .collect(Collectors.toList());

        return new OrderResponse.Order(
            id,
            userId,
            totalPrice,
            status,
            responseItems,
            createdAt,
            updatedAt
        );
    }

}
