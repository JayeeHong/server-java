package kr.hhplus.be.server.config.redis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedissonLockService {

    private final RedissonClient redissonClient;

    private static final long WAIT_TIME = 10L; //락 대기 최대 10초
    private static final long LEASE_TIME = 5L; //락 점유 5초


    public boolean tryLock(String key) {
        RLock lock = redissonClient.getLock(key);

        try {
            log.info("락 획득 key={}", key);
            // 락 대기 없이 락 획득 바로 시도
            return lock.tryLock(0, LEASE_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 획득 중 인터럽트 발생: {}", e);
        }
    }

    // 10초 간 대기 후 락 획득 시도
    public void lock(String key) {
        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);

            if (!available) {
                throw new IllegalStateException("락 획득 실패: " + key);
            }

            log.info("락 획득 성공 (key = {})", key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 시도 중 인터럽트 발생: ", e);
        }
    }

    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);

        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("락 해제 완료 (key = {})", key);
        }
    }
}
