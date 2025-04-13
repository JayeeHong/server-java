package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserApi {

    @Operation(summary = "사용자 잔액 충전")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "충전 성공"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
    UserResponse.Balance chargeBalance(
        @Parameter(description = "사용자 ID") Long userId,
        @Parameter(description = "충전 요청 DTO") UserRequest.Charge request
    );

    @Operation(summary = "사용자 잔액 조회")
    UserResponse.Balance getBalance(@Parameter(description = "사용자 ID") Long userId);
}
