package kr.hhplus.be.server.infrastructure.rank;

import java.time.Duration;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

@Component
public class RedisDailyRankCacheRepository implements RankCacheRepository {

    private final RedisTemplate<String, String> redis;
    private final ZSetOperations<String, String> zset;

    public RedisDailyRankCacheRepository(RedisTemplate<String, String> redis) {
        this.redis = redis;
        this.zset = redis.opsForZSet();
    }

    private static final Duration CACHE_TTL = Duration.ofHours(25);

    @Override
    public void incrementScore(String dateKey, String member, double delta) {
        zset.incrementScore(dateKey, member, delta);
        redis.expire(dateKey, CACHE_TTL);
    }

    @Override
    public Set<TypedTuple<String>> getSortedSet(String dateKey) {
        return zset.reverseRangeWithScores(dateKey, 0, -1);
    }

    @Override
    public void deleteKey(String dateKey) {
        redis.delete(dateKey);
    }
}
