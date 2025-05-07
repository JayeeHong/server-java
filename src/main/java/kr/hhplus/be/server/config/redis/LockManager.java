package kr.hhplus.be.server.config.redis;

public interface LockManager {

    boolean tryLock(String key);

    void unlock(String key);

}
