package kr.hhplus.be.server.application.rank;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.config.redis.CacheType;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.rank.RankCommand;
import kr.hhplus.be.server.domain.rank.RankInfo;
import kr.hhplus.be.server.domain.rank.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RankFacade {

    private final ProductService productService;
    private final OrderService orderService;
    private final RankService rankService;

    @Transactional
    public void createDailyRankAt(LocalDate date) {
        OrderCommand.DateQuery orderCommand = OrderCommand.DateQuery.of(date);
        OrderInfo.PaidItems paidProducts = orderService.getPaidItems(orderCommand);

        RankCommand.CreateList rankCommand = createListCommand(paidProducts, date);
        rankService.createSellRank(rankCommand);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheType.CacheName.POPULAR_PRODUCT, key = "'top:' + #criteria.top + ':days:' + #criteria.days")
    public RankResult.PopularProducts getPopularProducts(RankCriteria.PopularProducts criteria) {
        return getPopularProducts(criteria.getTop(), criteria.getDays());
    }

    @Transactional(readOnly = true)
    @CachePut(value = CacheType.CacheName.POPULAR_PRODUCT, key = "'top:' + #criteria.top + ':days:' + #criteria.days")
    public RankResult.PopularProducts updatePopularProducts(RankCriteria.PopularProducts criteria) {
        return getPopularProducts(criteria.getTop(), criteria.getDays());
    }

    private RankCommand.CreateList createListCommand(OrderInfo.PaidItems paidProducts, LocalDate yesterday) {
        List<RankCommand.Create> commands = paidProducts.getItems().stream()
            .map(item -> createCommand(item, yesterday))
            .toList();

        return RankCommand.CreateList.of(commands);
    }

    private RankCommand.Create createCommand(OrderInfo.PaidItem item, LocalDate yesterday) {
        return RankCommand.Create.of(
            item.getProductId(),
            item.getQuantity(),
            yesterday
        );
    }

    private RankResult.PopularProducts getPopularProducts(int top, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        RankCommand.PopularSellRank popularSellRankCommand = RankCommand.PopularSellRank.of(top, startDate, endDate);
        RankInfo.PopularProducts popularProducts = rankService.getPopularSellRank(popularSellRankCommand);

        ProductCommand.Products productsCommand = ProductCommand.Products.of(popularProducts.getProductIds());
        ProductInfo.Products products = productService.getProducts(productsCommand);

        return RankResult.PopularProducts.of(products.getProducts().stream()
            .map(this::toPopularProduct)
            .toList());
    }

    private RankResult.PopularProduct toPopularProduct(ProductInfo.Product product) {
        return RankResult.PopularProduct.builder()
            .productId(product.getProductId())
            .productName(product.getProductName())
            .productPrice(product.getProductPrice())
            .build();
    }
}
