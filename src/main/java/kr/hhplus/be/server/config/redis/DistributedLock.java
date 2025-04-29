package kr.hhplus.be.server.config.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key(); // 락을 걸 키
    long waitTime() default 10_000L; // 락 대기 시간 (ms)
    long leaseTime() default 5_000L; // 락 점유 시간 (ms)
}
