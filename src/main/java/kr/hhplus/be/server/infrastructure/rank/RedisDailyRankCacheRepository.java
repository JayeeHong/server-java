package kr.hhplus.be.server.infrastructure.rank;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
    public void incrementScore(String dateKey, String productId, double delta) {
        // Redis 트랜잭션을 열고, 두 연산을 묶어서 실행
        redis.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> Void execute(RedisOperations<K, V> ops) throws DataAccessException {
                ops.multi();
                // ZINCRBY
                ((RedisOperations<String, String>) ops)
                    .opsForZSet()
                    .incrementScore(dateKey, productId, delta);
                // EXPIRE
                ops.expire((K) dateKey, CACHE_TTL.getSeconds(), TimeUnit.SECONDS);
                ops.exec();
                return null;
            }
        });
    }

    @Override
    public Set<TypedTuple<String>> getSortedSet(String dateKey) {
        return redis.opsForZSet().reverseRangeWithScores(dateKey, 0, -1);
    }

    @Override
    public void deleteKey(String dateKey) {
        redis.delete(dateKey);
    }
}
