package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class OrderRequest {

    @Getter
    @Schema(description = "주문 상품 항목")
    public static class Item {

        @NotNull(message = "사용자 ID는 필수입니다.")
        @Schema(description = "상품 ID", example = "1")
        private final Long productId;

        @NotNull(message = "수량은 필수입니다.")
        @Schema(description = "주문 수량", example = "2")
        private final Integer quantity;

        public Item(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }

    @Getter
    @Schema(description = "주문 요청 본문")
    public static class Command {

        @NotNull(message = "사용자 ID는 필수입니다.")
        @Schema(description = "사용자 ID", example = "1")
        private final Long userId;

        @Schema(description = "사용할 쿠폰 ID (선택)", example = "10")
        private final Long couponId;

        @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
        @Schema(description = "주문 상품 목록")
        private final List<Item> items;

        public Command(Long userId, Long couponId, List<Item> items) {
            this.userId = userId;
            this.couponId = couponId;
            this.items = items;
        }
    }
}
