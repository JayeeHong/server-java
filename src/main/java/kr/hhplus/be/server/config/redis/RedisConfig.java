package kr.hhplus.be.server.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // Redis 연결을 위한 Connection 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    // Redis 데이터 처리를 위한 템플릿 구성
    // 해당 RedisTemplate을 통해서 데이터 통신으로 처리되는 직렬화 수행
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Redis 연결
        template.setConnectionFactory(redisConnectionFactory());

        // key-value 형태로 직렬화 수행
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // hash key-value 형태로 직렬화 수행
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        // 기본적으로 직렬화 수행 설정
        template.setDefaultSerializer(new StringRedisSerializer());

        return template;
    }

    // 리스트에 접근하여 다양한 연산 수행
    public ListOperations<String, Object> getListOperations() {
        return this.redisTemplate().opsForList();
    }

    // 단일 데이터에 접근하여 다양한 연산 수행
    public ValueOperations<String, Object> getValueOperations() {
        return this.redisTemplate().opsForValue();
    }

    // Redis 작업 중 등록, 수정, 삭제에 대한 처리 및 예외처리 수행
    public int executeOperation(Runnable operation) {
        try {
            operation.run();
            return 1;
        } catch (Exception e) {
            System.out.println("Redis 작업 오류 발생:: " + e.getMessage());
            return 0;
        }
    }

}
