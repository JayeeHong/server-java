package kr.hhplus.be.server.application.order;

import java.time.LocalDate;
import java.util.Optional;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.CouponInfo.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderCommand.Create;
import kr.hhplus.be.server.domain.order.OrderInfo.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductInfo.OrderItems;
import kr.hhplus.be.server.domain.product.ProductOrderedEvent;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserCouponInfo.UsableCoupon;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final OrderService orderService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public OrderResult.Order orderPayment(OrderCriteria.OrderPayment criteria) {

        userService.getUser(criteria.getUserId());

        OrderItems orderProducts = productService.getOrderProducts(criteria.toProductCommand());

        Optional<Long> couponId = Optional.ofNullable(criteria.getCouponId());
        Optional<UsableCoupon> usableCoupon = couponId.map(
            id -> userCouponService.getUsableCoupon(criteria.toCouponCommand()));
        Optional<Coupon> coupon = couponId.map(couponService::getCoupon);

        Create orderCommand = criteria.toOrderCommand(
            usableCoupon.map(UsableCoupon::getUserCouponId).orElse(null),
            coupon.map(Coupon::getDiscountAmount).orElse(0),
            orderProducts);

        Order order = orderService.createOrder(orderCommand);

        balanceService.useBalance(criteria.toBalanceCommand(order.getTotalPrice()));
        usableCoupon.ifPresent(c -> userCouponService.useUserCoupon(c.getUserCouponId()));
        productService.decreaseStock(criteria.toProductCommand());
        paymentService.pay(criteria.toPaymentCommand(order));
        orderService.paidOrder(order.getOrderId());

        // 커밋 후 캐싱을 위해 이벤트 발행
        publisher.publishEvent(
            new ProductOrderedEvent(orderProducts.getOrderItems(), LocalDate.now())
        );

        return OrderResult.Order.of(order);
    }

}
