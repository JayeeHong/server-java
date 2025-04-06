package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.interfaces.order.OrderRequest.CreateOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    @Override
    @PostMapping("/{userId}")
    public OrderResponse.Order createOrder(@PathVariable long userId, @RequestBody CreateOrder createOrder) {
        return orderFacade.registOrder(userId, createOrder);
    }
}
