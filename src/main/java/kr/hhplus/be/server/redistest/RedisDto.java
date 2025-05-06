package kr.hhplus.be.server.redistest;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RedisDto {

    private String key;
    private String value;
    private Duration duration;

}
