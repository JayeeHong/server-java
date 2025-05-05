package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import kr.hhplus.be.server.application.order.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderApi {

    private final OrderFacade orderFacade;

    public void orderPayment(@Valid @RequestBody OrderRequest.OrderPayment request) {
        orderFacade.orderPayment(request.toCriteria());
    }
}
