package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.infrastructure.order.OrderRepository;
import kr.hhplus.be.server.interfaces.order.OrderItemResponse.OrderItem;
import kr.hhplus.be.server.interfaces.order.OrderRequest.CreateOrder;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse.Order registOrder(long userId, CreateOrder createOrder) {

        // 사용자 식별자 유효성 체크

        // 주문할 금액 유효성 체크 (주문금액, 주문상품금액)

        // 사용자의 잔액 유효성 체크

        // 결제 처리 (사용자의 잔액 차감)

        // 주문 등록

        // 결제 성공 후 외부 데이터 플랫폼에 전송

        return new OrderResponse.Order(
            1L, 1L, 1000, OrderStatus.PAY_COMPLETE,
            List.of(new OrderItem(1L, 1L, 1L, 1, 1000)),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        );
    }

    // TODO 아래 기능을 나눠서 메소드 만들기
    // 주문 요청
    public OrderResponse.Order requestOrder(long userId, CreateOrder createOrder) {

        return new OrderResponse.Order(
            1L, 1L, 1000, OrderStatus.PAY_COMPLETE,
            List.of(new OrderItem(1L, 1L, 1L, 1, 1000)),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        );
    }

    // 결제 금액 계산 (잔액 유무 및 적용)
    public void calculateOrderAmt(long userId, CreateOrder createOrder) {

    }

}
