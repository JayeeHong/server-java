package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.coupon.CouponType;

public class CouponResponse {

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
}
