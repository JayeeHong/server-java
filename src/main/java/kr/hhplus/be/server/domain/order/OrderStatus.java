package kr.hhplus.be.server.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상태")
public enum OrderStatus {
    @Schema(description = "대기")
    WAIT("대기"),
    @Schema(description = "결제중")
    PAYING("결제중"),
    @Schema(description = "결제완료")
    PAY_COMPLETE("결제완료"),
    @Schema(description = "취소")
    CANCELLED("취소")
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(OrderStatus targetStatus) {
        return switch (this) {
            case WAIT -> targetStatus == PAYING || targetStatus == CANCELLED;
            case PAYING -> targetStatus == PAY_COMPLETE || targetStatus == CANCELLED;
            case PAY_COMPLETE -> false;
            case CANCELLED -> false;
        };
    }

}
