package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class OrderInfoEvent {

    private final Long orderId;
    private final Long userId;
    private final Long userCouponId;
    private final long totalPrice;
    private final long discountPrice;
    private final LocalDateTime paidAt;
    private final List<OrderInfoItemEvent> orderItems;

    private OrderInfoEvent(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.userCouponId = order.getUserCouponId();
        this.totalPrice = order.getTotalPrice();
        this.discountPrice = order.getDiscountPrice();
        this.paidAt = order.getPaidAt();

        List<OrderInfoItemEvent> result = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            result.add(new OrderInfoItemEvent(orderItem));
        }

        this.orderItems = result;
    }

    public static OrderInfoEvent toOrderInfoEvent(Order order) {
        return new OrderInfoEvent(order);
    }

    public class OrderInfoItemEvent {

        private final Long productId;
        private final String productName;
        private final Long unitPrice;
        private final int quantity;

        public OrderInfoItemEvent(OrderItem item) {
            this.productId = item.getProductId();
            this.productName = item.getProductName();
            this.unitPrice = item.getUnitPrice();
            this.quantity = item.getQuantity();
        }

    }

}
