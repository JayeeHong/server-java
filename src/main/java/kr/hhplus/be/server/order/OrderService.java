package kr.hhplus.be.server.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    public Order registOrder(long userId, Order order) {

        // 사용자 식별자 유효성 체크

        // 주문할 금액 유효성 체크 (주문금액, 주문상품금액)

        // 사용자의 잔액 유효성 체크

        // 결제 처리 (사용자의 잔액 차감)

        // 주문 등록

        // 결제 성공 후 외부 데이터 플랫폼에 전송

        return new Order(
            1L, 1L, 1000, OrderStatus.PAY_COMPLETE,
            List.of(new OrderItem(1L, 1L, 1L, 1, 1000)),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        );
    }

}
