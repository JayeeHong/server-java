package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 발급 이력")
public record CouponHistory(
    @Schema(description = "이력 ID", examples = "1")
    long id,
    @Schema(description = "사용자 ID", examples = "1")
    long userId, //사용자 id
    @Schema(description = "쿠폰 ID", examples = "1")
    long couponId, //쿠폰 id
    @Schema(description = "발행일", examples = "1743682862736")
    long issuedAt // 발행일
) {

}
