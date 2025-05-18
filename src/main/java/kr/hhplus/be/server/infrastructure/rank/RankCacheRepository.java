package kr.hhplus.be.server.infrastructure.rank;

import java.util.Set;
import org.springframework.data.redis.core.ZSetOperations;

public interface RankCacheRepository {

    void incrementScore(String dateKey, String productId, double delta);

    Set<ZSetOperations.TypedTuple<String>> getSortedSet(String dateKey);

    void deleteKey(String dateKey);

}
