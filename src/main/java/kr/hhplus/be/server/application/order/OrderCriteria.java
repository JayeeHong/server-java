package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderItems;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.user.UserCouponCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCriteria {

    @Getter
    public static class OrderPayment {

        private final Long userId;
        private final Long couponId;
        private final List<OrderItem> products;

        private OrderPayment(Long userId, Long couponId, List<OrderItem> products) {
            this.userId = userId;
            this.couponId = couponId;
            this.products = products;
        }

        public static OrderPayment of(Long userId, Long couponId, List<OrderItem> products) {
            return new OrderPayment(userId, couponId, products);
        }

        public ProductCommand.OrderItems toProductCommand() {
            return OrderItems.of(
                products.stream()
                    .map(o -> ProductCommand.OrderItem.of(o.getProductId(), o.getQuantity()))
                    .toList()
            );
        }

        public OrderCommand.Create toOrderCommand(Long userCouponId, int discountAmount, ProductInfo.OrderItems productInfo) {
            List<OrderCommand.OrderItem> orderProducts = productInfo.getOrderItems().stream()
                .map(p -> OrderCommand.OrderItem.of(p.getProductId(), p.getProductName(), p.getProductPrice(), p.getQuantity())).toList();

            return OrderCommand.Create.of(userId, userCouponId, discountAmount, orderProducts);
        }

        public UserCouponCommand.UsableCoupon toCouponCommand() {
            return UserCouponCommand.UsableCoupon.of(userId, couponId);
        }

        public BalanceCommand.Use toBalanceCommand(long totalPrice) {
            return BalanceCommand.Use.of(userId, totalPrice);
        }

        public PaymentCommand.Payment toPaymentCommand(OrderInfo.Order order) {
            return PaymentCommand.Payment.of(order.getOrderId(), userId, order.getTotalPrice());
        }
    }

    @Getter
    public static class OrderItem {

        private final Long productId;
        private final int quantity;

        private OrderItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public static OrderItem of(Long productId, int quantity) {
            return new OrderItem(productId, quantity);
        }

        public OrderCriteria.OrderItem toCriteria() {
            return OrderCriteria.OrderItem.of(productId, quantity);
        }
    }
}
