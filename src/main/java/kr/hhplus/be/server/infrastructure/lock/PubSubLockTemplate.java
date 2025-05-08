package kr.hhplus.be.server.infrastructure.lock;

import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.support.lock.LockCallback;
import kr.hhplus.be.server.support.lock.LockStrategy;
import kr.hhplus.be.server.support.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubLockTemplate implements LockTemplate {

    private final RedissonClient redissonClient;

    @Override
    public LockStrategy getLockStrategy() {
        return LockStrategy.PUB_SUB_LOCK;
    }

    @Override
    public <T> T executeWithLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, LockCallback<T> callback) throws Throwable {
        RLock lock = redissonClient.getLock(key);

        try {
            log.debug("락 획득 시도 : {}", key);
            boolean acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (!acquired) {
                throw new IllegalStateException("락 획득 실패 : " + key);
            }

            return callback.doInLock();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("락 해제 : {}", key);
            }
        }
    }
}
