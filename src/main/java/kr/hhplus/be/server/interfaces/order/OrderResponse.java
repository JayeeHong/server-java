package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class OrderResponse {

    @Getter
    @RequiredArgsConstructor
    @Schema(description = "주문 응답")
    public static class Summary {

        @Schema(description = "주문 ID", example = "101")
        private final Long orderId;

        @Schema(description = "사용자 ID", example = "1")
        private final Long userId;

        @Schema(description = "총 결제 금액", example = "9000")
        private final int totalAmount;

        @Schema(description = "사용된 쿠폰 ID", example = "10")
        private final Long usedCouponId;

        @Schema(description = "주문 상태", example = "WAIT")
        private final String status;

        @Schema(description = "주문 일시", example = "2024-04-13T18:00:00")
        private final LocalDateTime orderedAt;

        @Schema(description = "주문 상품 목록")
        private final List<OrderItemResponse.OrderItem> items;

        public static Summary from(Order order, List<OrderItem> orderItem, Coupon usedCoupon) {

            List<OrderItemResponse.OrderItem> items = orderItem.stream()
                .map(OrderItemResponse.OrderItem::from)
                .collect(Collectors.toList());

            return new Summary(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                usedCoupon != null ? usedCoupon.getId() : null,
                order.getOrderStatus().name(),
                order.getOrderedAt(),
                items
            );
        }
    }
}
