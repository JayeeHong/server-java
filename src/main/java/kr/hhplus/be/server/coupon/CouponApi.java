package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.product.Product;
import kr.hhplus.be.server.user.User;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Coupon", description = "쿠폰 관련 API")
public interface CouponApi {

    @Operation(summary = "선착순 쿠폰 발급")
    @Parameters({
        @Parameter(name = "userId", description = "사용자 ID", required = true),
        @Parameter(name = "couponId", description = "쿠폰 ID", required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = UserCoupon.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    UserCoupon issueCoupon(@PathVariable long userId, @PathVariable long couponId);

    @Operation(summary = "전체 쿠폰 조회")
    @Parameter(name = "userId", description = "사용자 ID", required = true)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserCoupon.class)))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<UserCoupon> getCoupons(@PathVariable long userId);

}
