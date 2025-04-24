package kr.hhplus.be.server.integration.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private User user;

    private Product product;

    private Balance balance;

    @BeforeEach
    void setUp() {

        user = User.create("userA");
        userRepository.save(user);

        product = Product.create("productA", 1000, 100);
        productRepository.save(product);

        balance = Balance.charge(user.getId(), 10000);
        balanceRepository.save(balance);
    }

    @Test
    @DisplayName("쿠폰 없이 주문을 생성한다")
    void placeOrderWithoutCoupon() {

        // given
        OrderRequest.Command command = OrderRequest.Command.of(user.getId(), null,
            List.of(OrderRequest.Item.of(product.getId(), 2)));

        // when
        OrderResponse.Result result = orderFacade.placeOrder(command);

        // then
        // 재고 확인
        Product findProduct = productRepository.findById(product.getId());
        assertThat(findProduct.getStock()).isEqualTo(98);

        // 잔액 확인
        int totalBalance = balanceRepository.getTotalBalance(user.getId());
        assertThat(totalBalance).isEqualTo(8000);

        // 주문 확인
        Order findOrder = orderRepository.findById(result.getOrderId());
        assertThat(findOrder.getTotalAmount()).isEqualTo(2000);
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.WAIT);

    }

    @Test
    @DisplayName("쿠폰을 적용하여 주문을 생성한다")
    void placeOrderWithCoupon() {

        // given
        Coupon coupon = Coupon.of(null, "1000원 할인", 1000, 10,
            LocalDateTime.of(9999, 4, 15, 20, 48));
        couponRepository.save(coupon);

        UserCoupon userCoupon = UserCoupon.create(user.getId(), coupon.getId(), LocalDateTime.now());
        userCouponRepository.save(userCoupon);

        OrderRequest.Command command = OrderRequest.Command.of(user.getId(), coupon.getId(),
            List.of(OrderRequest.Item.of(product.getId(), 2)));

        // when
        OrderResponse.Result result = orderFacade.placeOrder(command);

        // then
        // 재고 확인
        Product findProduct = productRepository.findById(product.getId());
        assertThat(findProduct.getStock()).isEqualTo(98);

        // 잔액 확인
        int totalBalance = balanceRepository.getTotalBalance(user.getId());
//        assertThat(totalBalance).isEqualTo(9000);

        // 주문 확인
        Order findOrder = orderRepository.findById(result.getOrderId());
//        assertThat(findOrder.getTotalAmount()).isEqualTo(1000);
//        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.WAIT);

        // 쿠폰 확인
//        assertThat(userCoupon.getUsedAt()).isNotNull();
    }
}
