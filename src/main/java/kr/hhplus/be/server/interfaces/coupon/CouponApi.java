package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "쿠폰 API", description = "선착순 쿠폰 발급 및 보유 목록 조회")
public interface CouponApi {

    @Operation(summary = "선착순 쿠폰 발급")
    CouponResponse.UserCoupon issueCoupon(
        @Parameter(description = "사용자 ID") @PathVariable Long userId,
        @Parameter(description = "쿠폰 ID") @PathVariable Long couponId
    );

    @Operation(summary = "사용자 보유 쿠폰 목록 조회")
    List<CouponResponse.UserCoupon> getUserCoupons(
        @Parameter(description = "사용자 ID") @PathVariable Long userId
    );
}
