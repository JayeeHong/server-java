package kr.hhplus.be.server.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Order", description = "주문 관련 API")
public interface OrderApi {

    @Operation(summary = "주문 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Order registOrder(@PathVariable long userId, @RequestBody Order order);

}
