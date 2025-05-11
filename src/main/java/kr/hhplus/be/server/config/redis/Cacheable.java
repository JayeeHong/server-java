package kr.hhplus.be.server.config.redis;

import java.time.Duration;

public interface Cacheable {

    String createKey(String key);

    String cacheName();

    Duration ttl();
}
