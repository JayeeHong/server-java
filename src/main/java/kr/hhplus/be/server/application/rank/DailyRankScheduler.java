package kr.hhplus.be.server.application.rank;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import kr.hhplus.be.server.domain.rank.DailyProductRank;
import kr.hhplus.be.server.infrastructure.rank.DailyProductRankRepository;
import kr.hhplus.be.server.infrastructure.rank.RankCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyRankScheduler {

    private final RankCacheRepository rankCacheRepository;
    private final DailyProductRankRepository dailyProductRankRepository;

    // 매일 오전 00:05에 어제 랭킹 집계 실행
    @Scheduled(cron = "0 5 0 * * *")
    public void flushDailyRanking() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        String key = "daily:ranking:" + yesterday;

        log.info("daily-ranking-scheduler-key::: {}", key);

        Set<ZSetOperations.TypedTuple<String>> entries = rankCacheRepository.getSortedSet(key);
        if (entries == null || entries.isEmpty()) {
            return;
        }

        List<DailyProductRank> batch = entries.stream()
            .map(t -> {
                DailyProductRank dailyProductRank = DailyProductRank.builder()
                    .rankDate(yesterday)
                    .productId(Long.valueOf(t.getValue()))
                    .score(t.getScore().longValue())
                    .build();
                return dailyProductRank;
            })
            .toList();

        dailyProductRankRepository.saveAll(batch);
    }
}
