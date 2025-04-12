package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.coupon.Coupon;
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

        public static Summary from(Order order, Coupon usedCoupon) {
            return new Summary(
                order.id(),
                order.userId(),
                order.totalAmount(),
                usedCoupon != null ? usedCoupon.id() : null,
                order.orderStatus().name(),
                order.orderedAt()
            );
        }
    }
}
