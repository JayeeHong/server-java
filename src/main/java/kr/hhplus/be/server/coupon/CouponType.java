package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 종류")
public enum CouponType {
    @Schema(description = "정률")
    PERCENTAGE("정률"),
    @Schema(description = "정액")
    FIXED_AMOUNT("정액");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
