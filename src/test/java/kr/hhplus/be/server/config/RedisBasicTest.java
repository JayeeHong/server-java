package kr.hhplus.be.server.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
public class RedisBasicTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("Redis 연결 및 기본 데이터 저장/조회 테스트")
    void redisConnectTest() {

        //given
        String key = "test:key";
        String value = "Hello Redis!";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // when
        valueOperations.set(key, value, 10, TimeUnit.SECONDS); //TTL 10초 설정

        String savedValue = valueOperations.get(key);

        // then
        assertThat(savedValue).isEqualTo(value);
        log.info("redis 저장 값 확인: {}", savedValue);
    }

}
