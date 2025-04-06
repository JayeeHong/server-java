package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 정보")
public class OrderItemResponse {
    public record OrderItem(
        @Schema(description = "주문 상품 ID", example = "1")
        long id,
        @Schema(description = "주문 ID", example = "1")
        long orderId,
        @Schema(description = "상품 ID", example = "1")
        long productId,
        @Schema(description = "주문 상품 갯수", example = "10")
        int quantity,
        @Schema(description = "주문 상품 금액 (상품금액 * 갯수)", example = "100")
        int price
    ) {

    }
}