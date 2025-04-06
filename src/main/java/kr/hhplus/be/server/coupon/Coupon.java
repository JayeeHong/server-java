package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 정보")
public record Coupon(
    @Schema(description = "쿠폰 ID", examples = "1")
    long id,
    @Schema(description = "쿠폰 코드", examples = "COU-20250403001(형식은 미정)")
    String code,
    @Schema(description = "할인금액 or 할인율", examples = {"20", "1000"})
    int discount,
    @Schema(description = "쿠폰타입 (정액인지, 정률인지)", examples = {"PERCENTAGE", "FIXED_AMOUNT"})
    CouponType type,
    @Schema(description = "쿠폰발행 종료일", examples = "1743682862736")
    long issueEndDt,
    @Schema(description = "쿠폰발행 갯수", examples = "100")
    int issueCnt,
    @Schema(description = "상품 ID (특정 상품용 쿠폰인 경우)", examples = "2")
    Long productId
) {

}
