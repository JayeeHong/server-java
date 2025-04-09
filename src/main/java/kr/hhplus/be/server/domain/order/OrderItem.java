package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.interfaces.order.OrderItemResponse;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private int price;

    public OrderItem(Long id, Long orderId, Long productId, int quantity, int price) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemResponse.OrderItem toResponse() {
        return new OrderItemResponse.OrderItem(id, orderId, productId, quantity, price);
    }
}
