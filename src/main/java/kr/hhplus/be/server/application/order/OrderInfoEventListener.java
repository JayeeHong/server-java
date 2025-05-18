package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderExternalClient;
import kr.hhplus.be.server.domain.order.OrderInfoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderInfoEventListener {

    private final OrderExternalClient orderExternalClient;

    @Async("orderEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderSuccessHandler(OrderInfoEvent order) {

        log.info("주문 정보 이벤트 발행");

        orderExternalClient.sendOrderMessage(order);
    }

}
