package kr.hhplus.be.server.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "주문 상태")
public enum OrderStatus {
    @Schema(description = "주문생성")
    CREATED("주문생성"),
    @Schema(description = "결제완료")
    PAY_COMPLETE("결제완료"),
    @Schema(description = "취소")
    CANCELLED("취소"),
    ;

    private final String description;

    public boolean canTransitionTo(OrderStatus targetStatus) {
        return switch (this) {
            case CREATED -> targetStatus == PAY_COMPLETE || targetStatus == CANCELLED;
            case PAY_COMPLETE -> false;
            case CANCELLED -> false;
        };
    }

}
