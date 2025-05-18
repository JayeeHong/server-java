package kr.hhplus.be.server.application.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.util.List;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderExternalClient;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.UserCommand.Create;
import kr.hhplus.be.server.domain.user.UserCouponService;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Import(OrderFacadeSyncTest.SyncAsyncConfig.class)
class OrderFacadeSyncTest extends IntegrationTestSupport {

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

    @MockitoSpyBean
    OrderExternalClient orderExternalClient;

    UserInfo.User user;

    ProductInfo.Product product;

    BalanceCommand.Save balance;

    @BeforeEach
    void setUp() {
        user = userService.createUser(Create.of("userA"));

        balance = BalanceCommand.Save.of(user.getUserId(), 1_000_000L);
        balanceService.saveBalance(balance);

        ProductCommand.Create command = ProductCommand.Create.of("productA", 10_000L, 100, ProductStatus.SELLING);
        product = productService.saveProduct(command);
    }

    @TestConfiguration
    static class SyncAsyncConfig {
        @Bean
        public TaskExecutor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void orderPaymentSyncTest() {
        // given
        OrderCriteria.OrderPayment criteria = OrderCriteria.OrderPayment.of(user.getUserId(), null,
            List.of(OrderCriteria.OrderItem.of(product.getProductId(), 2))
        );

        // when
        orderFacade.orderPayment(criteria);

        // then
        verify(orderExternalClient).sendOrderMessage(any());
    }

}
