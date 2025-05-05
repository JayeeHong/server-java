package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequest {

    @Getter
    @NoArgsConstructor
    public static class OrderPayment {

        @NotNull(message = "사용자 ID는 필수 입니다.")
        private Long userId;
        private Long couponId;

        @Valid
        @NotEmpty(message = "상품 목록은 1개 이상이여야 합니다.")
        private List<OrderItem> products;

        private OrderPayment(Long userId, Long couponId, List<OrderItem> products) {
            this.userId = userId;
            this.couponId = couponId;
            this.products = products;
        }

        public static OrderPayment of(Long userId, Long couponId, List<OrderItem> products) {
            return new OrderPayment(userId, couponId, products);
        }

        public OrderCriteria.OrderPayment toCriteria() {
            return OrderCriteria.OrderPayment.of(userId, couponId, products.stream()
                    .map(OrderItem::toCriteria)
                    .toList());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class OrderItem {

        @NotNull(message = "상품 ID는 필수입니다.")
        private Long id;

        @NotNull(message = "상품 구매 수량은 필수입니다.")
        @Positive(message = "상품 구매 수량은 양수여야 합니다.")
        private Integer quantity;

        private OrderItem(Long id, Integer quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public static OrderItem of(Long id, Integer quantity) {
            return new OrderItem(id, quantity);
        }

        public OrderCriteria.OrderItem toCriteria() {
            return OrderCriteria.OrderItem.of(id, quantity);
        }
    }

}
