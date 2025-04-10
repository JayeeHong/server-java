package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.order.OrderRequest.CreateOrder;
import org.springframework.web.ErrorResponse;

@Tag(name = "Order", description = "주문 관련 API")
public interface OrderApi {

    @Operation(summary = "주문 등록")
    @Parameter(name = "userId", description = "사용자 ID", required = true)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    OrderResponse.Order createOrder(long userId, CreateOrder createOrder);

}
