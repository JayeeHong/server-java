package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderInfoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderInfoEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(OrderInfoEvent order) {
        applicationEventPublisher.publishEvent(order);
    }

}
