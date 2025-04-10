package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderStatus;

@Schema(description = "주문 정보")
public class OrderRequest {
    public record CreateOrder(
        @Schema(description = "주문 ID", example = "1")
        long id,
        @Schema(description = "사용자 ID", example = "1")
        long userId,
        @Schema(description = "주문 총 금액", example = "1000")
        int totalPrice,
        @Schema(description = "주문 상태", example = "PAY_COMPLETE", implementation = OrderStatus.class)
        OrderStatus status,
        @Schema(description = "주문 상품 목록")
        @ArraySchema(schema = @Schema(implementation = OrderItemRequest.CreateOrderItem.class))
        List<OrderItemRequest.CreateOrderItem> orderItems,
        @Schema(description = "사용자 쿠폰 ID")
        Long userCouponId,
        @Schema(description = "생성시간", example = "1743682862736")
        long createdAt,
        @Schema(description = "수정시간", example = "1743682862736")
        long updatedAt
    ) {

    }
}