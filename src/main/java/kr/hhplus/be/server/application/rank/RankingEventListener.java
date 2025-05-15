package kr.hhplus.be.server.application.rank;

import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductOrderedEvent;
import kr.hhplus.be.server.infrastructure.rank.RankCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingEventListener {

    private final RankCacheRepository cacheRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductOrderedEvent event) {
        String key = "daily:ranking:" + event.getOrderDate();

        log.info("product-daily-ranking-key::: {}", key);

        for (ProductInfo.OrderItem item : event.getItems()) {
            try {
                cacheRepository.incrementScore(key, item.getProductId().toString(),
                    item.getQuantity());
            } catch (Exception e) {
                // 캐싱 실패시에도 주문 트랜잭션에는 영향을 주지 않는다.
                log.error("Redis ranking cache failed for key={}, member={}", key,
                    item.getProductId(), e);
            }
        }
    }
}
