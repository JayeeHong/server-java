package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.user.UserCouponResponse.Coupons;

@Tag(name = "User", description = "사용자 쿠폰 API")
public interface UserCouponApi {

    @Operation(summary = "사용자 쿠폰 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
    Coupons getCoupons(
        @Parameter(description = "사용자 ID") Long id
    );

    @Operation(summary = "사용자 쿠폰 발행")
    void chargeBalance(
        @Parameter(description = "사용자 ID") Long userId,
        @Parameter(description = "발행 요청 DTO") UserCouponRequest.Publish request
    );
}
