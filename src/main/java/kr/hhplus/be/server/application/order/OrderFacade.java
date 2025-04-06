package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.coupon.CouponService;
import kr.hhplus.be.server.interfaces.order.OrderRequest.CreateOrder;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Order;
import kr.hhplus.be.server.application.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final CouponService couponService;
    private final UserService userService;

    public OrderResponse.Order registOrder(long userId, CreateOrder createOrder) {

        // 1. 주문 요청
        Order order = orderService.requestOrder(userId, createOrder);
        // 2. 사용자의 쿠폰 유무 조회 및 적용 (TODO 쿠폰 적용하여 계산)
        couponService.getCoupon(userId, 1L);
        // 3. 사용자의 결제 금액 조회 및 계산 (TODO 결제 금액에서 주문 금액 계산)
        userService.getBalance(userId);

        return null;
    }

}
