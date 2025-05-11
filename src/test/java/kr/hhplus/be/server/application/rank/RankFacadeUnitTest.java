package kr.hhplus.be.server.application.rank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.rank.RankInfo;
import kr.hhplus.be.server.domain.rank.RankService;
import kr.hhplus.be.server.support.MockTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class RankFacadeUnitTest extends MockTestSupport {

    @InjectMocks
    private RankFacade rankFacade;

    @Mock
    private OrderService orderService;

    @Mock
    private RankService rankService;

    @Mock
    private ProductService productService;

    @DisplayName("일별 랭킹을 생성한다.")
    @Test
    void createDailyRankAt() {
        // given
        OrderInfo.PaidItems paidProducts = OrderInfo.PaidItems.of(
            List.of(
                OrderInfo.PaidItem.of(1L, 10),
                OrderInfo.PaidItem.of(2L, 20)
            )
        );

        when(orderService.getPaidItems(any()))
            .thenReturn(paidProducts);

        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when
        rankFacade.createDailyRankAt(yesterday);

        // then
        InOrder inOrder = inOrder(orderService, rankService);
        inOrder.verify(orderService, times(1)).getPaidItems(any());
        inOrder.verify(rankService, times(1)).createSellRank(any());
    }

    @DisplayName("최근 3일 가장 많이 팔린 상위 상품 5개를 조회한다.")
    @Test
    void getPopularProducts() {
        // given
        RankInfo.PopularProducts rankPopularProducts = RankInfo.PopularProducts.of(List.of(
            RankInfo.PopularProduct.of(1L, 120L),  // 1등 상품
            RankInfo.PopularProduct.of(2L, 95L),   // 2등 상품
            RankInfo.PopularProduct.of(3L, 87L),   // 3등 상품
            RankInfo.PopularProduct.of(4L, 76L),   // 4등 상품
            RankInfo.PopularProduct.of(5L, 65L)   // 5등 상품
        ));
        when(rankService.getPopularSellRank(any()))
            .thenReturn(rankPopularProducts);

        ProductInfo.Products products = ProductInfo.Products.of(List.of(
            ProductInfo.Product.of(1L, "productA", 1_000L, 10),
            ProductInfo.Product.of(2L, "productB", 2_000L, 10),
            ProductInfo.Product.of(3L, "productC", 3_000L, 10),
            ProductInfo.Product.of(4L, "productD", 4_000L, 10),
            ProductInfo.Product.of(5L, "productE", 5_000L, 10)
        ));

        when(productService.getProducts(any()))
            .thenReturn(products);

        // when
        RankResult.PopularProducts popularProducts = rankFacade.getPopularProducts(RankCriteria.PopularProducts.ofTop5Days3());

        // then
        InOrder inOrder = inOrder(rankService, productService);
        inOrder.verify(rankService, times(1)).getPopularSellRank(any());
        inOrder.verify(productService, times(1)).getProducts(any());

        assertThat(popularProducts.getProducts()).hasSize(5)
            .extracting("productId", "productName", "productPrice")
             .containsExactly(
                tuple(1L, "productA", 1_000L),
                tuple(2L, "productB", 2_000L),
                tuple(3L, "productC", 3_000L),
                tuple(4L, "productD", 4_000L),
                tuple(5L, "productE", 5_000L)
            );
    }

}