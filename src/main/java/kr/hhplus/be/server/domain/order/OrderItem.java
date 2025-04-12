package kr.hhplus.be.server.domain.order;

public class OrderItem {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final int quantity;

    private OrderItem(Long id, Long orderId, Long productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static OrderItem of(Long id, Long orderId, Long productId, int quantity) {
        return new OrderItem(id, orderId, productId, quantity);
    }

    public Long id() {
        return id;
    }

    public Long orderId() {
        return orderId;
    }

    public Long productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }
}
