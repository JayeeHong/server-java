package kr.hhplus.be.server.application.order;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderFacadeUnitTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private UserCouponService userCouponService;

    @Mock
    private CouponService couponService;

    @Mock
    private OrderService orderService;

    @Mock
    private BalanceService balanceService;

    @Mock
    private PaymentService paymentService;

    @Test
    @DisplayName("주문 및 결제를 한다.")
    void orderPayment() {
        // given
        OrderCriteria.OrderPayment criteria = mock(OrderCriteria.OrderPayment.class);
        ProductInfo.OrderItems mockOrderProducts = mock(ProductInfo.OrderItems.class);
        UserCouponInfo.UsableCoupon mockUsableCoupon = mock(UserCouponInfo.UsableCoupon.class);
        CouponInfo.Coupon mockCoupon = mock(CouponInfo.Coupon.class);
        OrderInfo.Order mockOrder = mock(OrderInfo.Order.class);

        when(productService.getOrderProducts(any())).thenReturn(mockOrderProducts);
        when(userCouponService.getUsableCoupon(any())).thenReturn(mockUsableCoupon);
        when(couponService.getCoupon(any())).thenReturn(mockCoupon);
        when(orderService.createOrder(any())).thenReturn(mockOrder);

        // when
        orderFacade.orderPayment(criteria);

        // then
        InOrder inOrder = inOrder(userService,
            productService,
            userCouponService,
            couponService,
            orderService,
            orderService,
            balanceService,
            paymentService
        );

        inOrder.verify(userService, times(1)).getUser(criteria.getUserId());
        inOrder.verify(productService, times(1)).getOrderProducts(criteria.toProductCommand());
        inOrder.verify(userCouponService, times(1)).getUsableCoupon(criteria.toCouponCommand());
        inOrder.verify(couponService, times(1)).getCoupon(criteria.getCouponId());
        inOrder.verify(orderService, times(1)).createOrder(criteria.toOrderCommand(
            mockUsableCoupon.getUserCouponId(),
            mockCoupon.getDiscountAmount(),
            mockOrderProducts
        ));
        inOrder.verify(balanceService, times(1)).useBalance(criteria.toBalanceCommand(
            mockOrder.getTotalPrice()
        ));
        inOrder.verify(userCouponService, times(1)).useUserCoupon(mockUsableCoupon.getUserCouponId());
        inOrder.verify(productService, times(1)).decreaseStock(criteria.toProductCommand());
        inOrder.verify(paymentService, times(1)).pay(criteria.toPaymentCommand(mockOrder));
        inOrder.verify(orderService, times(1)).paidOrder(mockOrder.getOrderId());
    }
}