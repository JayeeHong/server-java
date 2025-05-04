package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCommand {

    @Getter
    public static class Create {
        private final Long userId;
        private final Long userCouponId;
        private final long discountPrice;
        private List<OrderItem> items;

        private Create(Long userId, Long userCouponId, long discountPrice, List<OrderItem> items) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.discountPrice = discountPrice;
            this.items = items;
        }

        public static Create of(Long userId, Long userCouponId, long discountPrice, List<OrderItem> items) {
            return new Create(userId, userCouponId, discountPrice, items);
        }
    }

    @Getter
    public static class OrderItem {

        private final Long productId;
        private final String productName;
        private final Long unitPrice;
        private final int quantity;

        private OrderItem(Long productId, String productName, Long unitPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public static OrderItem of(Long productId, String productName, Long unitPrice, int quantity) {
            return new OrderItem(productId, productName, unitPrice, quantity);
        }

        public static OrderItem toOrderItem(kr.hhplus.be.server.domain.order.OrderItem orderItem) {
            return OrderItem.of(orderItem.getProductId(), orderItem.getProductName(), orderItem.getUnitPrice(), orderItem.getQuantity());
        }
    }

    @Getter
    public static class PaidItem {

        private final Long productId;
        private final int quantity;

        public PaidItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public static PaidItem of(Long productId, int quantity) {
            return new PaidItem(productId, quantity);
        }
    }

    @Getter
    public static class PaidItems {

        private final LocalDate paidAt;
        private final OrderStatus status;

        private PaidItems(LocalDate paidAt, OrderStatus status) {
            this.paidAt = paidAt;
            this.status = status;
        }

        public static PaidItems of(LocalDate paidAt, OrderStatus status) {
            return new PaidItems(paidAt, status);
        }
    }

    @Getter
    public static class DateQuery {

        private final LocalDate date;

        private DateQuery(LocalDate date) {
            this.date = date;
        }

        public static DateQuery of(LocalDate date) {
            return new DateQuery(date);
        }

        public PaidItems toPaidProductsQuery(OrderStatus status) {
            return PaidItems.of(date, status);
        }
    }
}
