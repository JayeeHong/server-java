package kr.hhplus.be.server.application.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.UserCommand.Create;
import kr.hhplus.be.server.domain.user.UserCouponCommand;
import kr.hhplus.be.server.domain.user.UserCouponInfo;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private PaymentService paymentService;

    UserInfo.User user;

    ProductInfo.Product product;

    BalanceCommand.Save balance;

    @BeforeEach
    void setUp() {
        user = userService.createUser(Create.of("userA"));

        balance = BalanceCommand.Save.of(user.getUserId(), 1_000_000L);
        System.out.println(user.getUserId());
        balanceService.saveBalance(balance);

        ProductCommand.Create command = ProductCommand.Create.of("productA", 10_000L, 100, ProductStatus.SELLING);
        product = productService.saveProduct(command);
    }

    @Test
    @DisplayName("쿠폰 없이 주문 결제를 한다.")
    void orderPaymentWithoutCoupon() {
        // given
        OrderCriteria.OrderPayment criteria = OrderCriteria.OrderPayment.of(user.getUserId(), null,
            List.of(OrderCriteria.OrderItem.of(product.getProductId(), 2))
        );

        // when
        OrderResult.Order result = orderFacade.orderPayment(criteria);

        // then
        BalanceInfo.Balance findBalance = balanceService.getBalance(user.getUserId());
        assertThat(findBalance.getAmount()).isEqualTo(980_000L);

        ProductInfo.Product findProduct = productService.getProduct(product.getProductId());
        assertThat(findProduct.getQuantity()).isEqualTo(98);

        OrderInfo.Order order = orderService.getOrder(result.getOrderId());
        assertThat(order.getTotalPrice()).isEqualTo(20_000L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAY_COMPLETE);
    }

    @Test
    @DisplayName("쿠폰이 있는 주문 결제를 한다.")
    void orderPaymentWithCoupon() {
        // given
        CouponCommand.Create coupon = CouponCommand.Create.of("couponA", 1000, 100,
            LocalDateTime.of(9999, 12, 31, 23, 59, 59));
        CouponInfo.Coupon savedCoupon = couponService.saveCoupon(coupon);

        UserCouponCommand.Publish userCoupon = UserCouponCommand.Publish.of(user.getUserId(), savedCoupon.getCouponId());
        UserCouponInfo.Coupon savedUserCoupon = userCouponService.createUserCoupon(userCoupon);

        OrderCriteria.OrderPayment criteria = OrderCriteria.OrderPayment.of(user.getUserId(), savedCoupon.getCouponId(),
            List.of(OrderCriteria.OrderItem.of(product.getProductId(), 2))
        );

        // when
        OrderResult.Order result = orderFacade.orderPayment(criteria);

        // then
        BalanceInfo.Balance findBalance = balanceService.getBalance(user.getUserId());
        assertThat(findBalance.getAmount()).isEqualTo(981_000L);

        UserCouponInfo.Coupon findUserCoupon = userCouponService.getUserCoupon(user.getUserId(),
            savedCoupon.getCouponId());
        assertThat(findUserCoupon.getUsedAt()).isNotNull();

        ProductInfo.Product findProduct = productService.getProduct(product.getProductId());
        assertThat(findProduct.getQuantity()).isEqualTo(98);

        OrderInfo.Order order = orderService.getOrder(result.getOrderId());
        assertThat(order.getTotalPrice()).isEqualTo(19_000L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAY_COMPLETE);
    }
}