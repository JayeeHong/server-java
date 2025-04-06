package kr.hhplus.be.server.domain.order;

import java.util.List;

public record Order(
    long id,
    long userId,
    int totalPrice,
    OrderStatus status,
    List<OrderItem> orderItems,
    long createdAt,
    long updatedAt
) {

}
