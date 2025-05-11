package kr.hhplus.be.server.config.redis;

import lombok.Getter;

@Getter
public class RedissonResultDto {

    private String message;
    private boolean successYn;

    private RedissonResultDto(String message, boolean successYn) {
        this.message = message;
        this.successYn = successYn;
    }

    public static RedissonResultDto of(String message, boolean successYn) {
        return new RedissonResultDto(message, successYn);
    }
}
