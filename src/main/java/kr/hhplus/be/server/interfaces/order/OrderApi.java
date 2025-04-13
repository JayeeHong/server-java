package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "주문 API", description = "주문 및 결제 관련 기능")
public interface OrderApi {

    @Operation(summary = "주문 생성 및 결제")
    OrderResponse.Summary placeOrder(@RequestBody OrderRequest.Command request);
}
