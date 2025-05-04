package kr.hhplus.be.server.domain.order;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfo {

    @Getter
    public static class Order {

        private final Long orderId;
        private final long totalPrice;
        private final long discountPrice;

        private Order(Long orderId, long totalPrice, long discountPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.discountPrice = discountPrice;
        }

        public static Order of(Long orderId, long totalPrice, long discountPrice) {
            return new Order(orderId, totalPrice, discountPrice);
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

        private final List<PaidItem> items;

        private PaidItems(List<PaidItem> items) {
            this.items = items;
        }

        public static PaidItems of(List<PaidItem> items) {
            return new PaidItems(items);
        }
    }
}
