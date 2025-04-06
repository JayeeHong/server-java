package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.web.ErrorResponse;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserApi {

    @Operation(summary = "잔액 충전")
    @Parameters({
        @Parameter(name = "userId", description = "사용자 ID", required = true),
        @Parameter(name = "amount", description = "사용 금액", required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    UserResponse.User chargeBalance(long userId, int amount);

    @Operation(summary = "잔액 조회")
    @Parameter(name = "userId", description = "사용자 ID", required = true)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    UserResponse.User getBalance(long userId);

}
