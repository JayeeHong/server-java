package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;

@Schema(description = "주문 상품 정보")
public class OrderItemResponse {
    public record OrderItem(
        @Schema(description = "주문 상품 ID", example = "1")
        Long id,
        @Schema(description = "주문 ID", example = "1")
        Long orderId,
        @Schema(description = "상품 ID", example = "1")
        Long productId,
        @Schema(description = "주문 상품 갯수", example = "10")
        int quantity,
        @Schema(description = "주문 상품 금액 (상품금액 * 갯수)", example = "100")
        int price
    ) {

        public static OrderItem from(kr.hhplus.be.server.domain.order.OrderItem item) {
            return new OrderItem(
                item.getId(),
                item.getOrder().getId(),
                item.getProductId(),
                item.getQuantity(),
                1000
            );
        }
    }
}