package kr.hhplus.be.server.domain.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

public record Coupon(
    long id,
    String code,
    int discount,
    CouponType type,
    long issueEndDt,
    int issueCnt,
    Long productId
) {

}
