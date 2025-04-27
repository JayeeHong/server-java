package kr.hhplus.be.server.integration.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Command;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Item;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderIntegrationLockTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    @DisplayName("주문 등록 Redisson 락 테스트 - 정상 주문 처리")
    void placeOrderWithLockSuccessTest() {

        // given
        // 1. 상품 등록
        Product product = Product.create("productA", 10000, 100);
        productRepository.save(product);

        // 2. 사용자 등록
        User user = User.create("userA");
        userRepository.save(user);
        // 2-1. 사용자 잔액 충전
        Balance balance = Balance.charge(user.id(), 1000000);
        balanceRepository.save(balance);

        // 3. 주문 요청 생성
        List<Item> items = List.of(new Item(product.getId(), 2)); // 상품 2개 주문
        Command command = Command.of(user.id(), null, items);

        // when
        Result result = orderFacade.placeOrderWithLock(command);

        // then
        // 주문 검증
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotalAmount()).isEqualTo(20000);
        assertThat(result.getUsedCouponId()).isNull(); // 쿠폰 사용하지 않음

        // 상품, 잔액 검증
        Product findProduct = productRepository.findById(product.getId());
        int userBalance = balanceRepository.getTotalBalance(user.id());

        assertThat(findProduct.getStock()).isEqualTo(98);
        assertThat(userBalance).isEqualTo(980000);
    }

}
