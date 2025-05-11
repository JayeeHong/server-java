package kr.hhplus.be.server.redistest;

import java.time.Duration;
import kr.hhplus.be.server.config.redis.RedisConfig;
import kr.hhplus.be.server.config.redis.RedisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Redis 단일 데이터 처리 비즈니스 로직 구현체
 */
@Service
@RequiredArgsConstructor
public class RedisSingleDataServiceImpl implements RedisSingleDataService {

    private final RedisHandler redisHandler;
    private final RedisConfig redisConfig;

    // Redis 단일 데이터 값 등록/수정
    @Override
    public int setSingleData(String key, Object value) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value));
    }

    // Redis 단일 데이터 값 등록/수정 (duration 값이 존재하면 메모리 상 유효시간 지정)
    @Override
    public int setSingleData(String key, Object value, Duration duration) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value, duration));
    }

    // Redis 키를 기반으로 단일 데이터 값 조회
    @Override
    public String getSingleData(String key) {
        if (redisHandler.getValueOperations().get(key) == null) {
            return "";
        }

        return String.valueOf(redisHandler.getValueOperations().get(key));
    }

    // Redis 키를 기반으로 단일 데이터 값 삭제
    @Override
    public int deleteSingleData(String key) {
        return redisHandler.executeOperation(() -> redisConfig.redisTemplate().delete(key));
    }
}
