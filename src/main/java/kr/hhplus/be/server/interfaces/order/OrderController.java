package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Command;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderApi {

    @Override
    public Result placeOrder(Command request) {
        return null;
    }
}
