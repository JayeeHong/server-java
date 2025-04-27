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
public class RedisTest {

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
    
    @Test
    @DisplayName("Redis TTL 테스트 - 시간이 지나면 데이터가 사리져야 한다")
    void redisTtlTest() throws InterruptedException {
        
        // given
        String key = "ttl:test:key";
        String value = "expire!!";

        redisTemplate.opsForValue().set(key, value, 3, TimeUnit.SECONDS); //TTL 3초
        
        // when
        String immediatelySavedValue = redisTemplate.opsForValue().get(key);
        Thread.sleep(4000); // TLL보다 길게 설정

        String savedValue = redisTemplate.opsForValue().get(key);
        
        // then
        assertThat(immediatelySavedValue).isEqualTo(value); //저장 직후에는 값 존재
        assertThat(savedValue).isNull(); //TTL 지난 후에는 값 없음
    }

}
