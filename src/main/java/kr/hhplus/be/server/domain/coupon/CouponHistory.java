package kr.hhplus.be.server.domain.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

public record CouponHistory(
    long id,
    long userId, //사용자 id
    long couponId, //쿠폰 id
    long issuedAt // 발행일
) {

}
