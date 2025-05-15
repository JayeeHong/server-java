package kr.hhplus.be.server.application.rank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.UserCommand.Create;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.infrastructure.rank.DailyProductRankRepository;
import kr.hhplus.be.server.infrastructure.rank.RankCacheRepository;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.support.database.RedisCacheCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class DailyRankingIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RankCacheRepository rankCacheRepository;

    @Autowired
    private DailyProductRankRepository dailyProductRankRepository;

    @Autowired
    private RedisCacheCleaner redisCacheCleaner;

    @AfterEach
    void tearDown() {
        redisCacheCleaner.clean();
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void orderPaymentDailyRanking() {

        UserInfo.User user = userService.createUser(Create.of("userA"));

        BalanceCommand.Save balance = BalanceCommand.Save.of(user.getUserId(), 1_000_000L);
        balanceService.saveBalance(balance);

        ProductCommand.Create command = ProductCommand.Create.of("productA", 10_000L, 100, ProductStatus.SELLING);
        ProductInfo.Product product = productService.saveProduct(command);

        // 주문 3건 발생 (위에서 생성한 상품으로, 수량 1씩)
        OrderCriteria.OrderPayment criteria = OrderCriteria.OrderPayment.of(user.getUserId(), null,
            List.of(OrderCriteria.OrderItem.of(product.getProductId(), 1))
        );

        orderFacade.orderPayment(criteria);
        orderFacade.orderPayment(criteria);
        orderFacade.orderPayment(criteria);

        // redis에 누적됐는지 확인
        String key = "daily:ranking:" + LocalDate.now(ZoneId.of("Asia/Seoul"));

        Optional<TypedTuple<String>> opt = rankCacheRepository.getSortedSet(key).stream()
            .filter(t -> t.getValue().equals(String.valueOf(product.getProductId())))
            .findFirst();
        assertTrue(opt.isPresent(), "");
        Double score = opt.get().getScore();

        assertThat(score).isGreaterThanOrEqualTo(score);
    }
}
