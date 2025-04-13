package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import kr.hhplus.be.server.application.order.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderApi {

    private final OrderFacade orderFacade;

    @Override
    @PostMapping
    public OrderResponse.Summary placeOrder(@RequestBody @Valid OrderRequest.Command request) {
        return orderFacade.placeOrder(request);
    }
}
