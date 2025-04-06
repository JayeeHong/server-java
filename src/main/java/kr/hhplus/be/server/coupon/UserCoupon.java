package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자별 쿠폰 정보")
public record UserCoupon(
    @Schema(description = "사용자 ID", examples = "1")
    long user_id,
    @Schema(description = "쿠폰 코드", examples = "COU-20250403001(형식은 미정)")
    String coupon_code,
    @Schema(description = "할인금액 or 할인율", examples = {"20", "1000"})
    int discount,
    @Schema(description = "쿠폰타입 (정액인지, 정률인지)", examples = {"PERCENTAGE", "FIXED_AMOUNT"})
    CouponType type,
    @Schema(description = "사용여부", examples = "true")
    boolean isUsed,
    @Schema(description = "발행일", examples = "1743682862736")
    long issued_at,
    @Schema(description = "만료일", examples = "1743682862736")
    long expired_at
) {

}
