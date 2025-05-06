package kr.hhplus.be.server.redistest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis 단일 데이터 조회, 등록, 삭제 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/redis/singleData")
@RequiredArgsConstructor
public class RedisSingleDataController {

    private final RedisSingleDataService redisSingleDataService;

    /**
     * Redis Key 를 기반으로 단일 데이터 값 조회
     */
    @PostMapping("/getValue")
    public ResponseEntity<Object> getValue(@RequestBody RedisDto redisDto) {
        String result = redisSingleDataService.getSingleData(redisDto.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Redis 단일 데이터 값을 등록/수정
     * (duration 값이 존재하면 메모리상 유효시간 지정)
     */
    @PostMapping("/setValue")
    public ResponseEntity<Object> setValue(@RequestBody RedisDto redisDto) {
        int result = 0;
        if (redisDto.getDuration() == null) {
            result = redisSingleDataService.setSingleData(redisDto.getKey(), redisDto.getValue());
        } else {
            result = redisSingleDataService.setSingleData(redisDto.getKey(), redisDto.getValue(), redisDto.getDuration());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Redis 키 기반으로 단일 데이터 값 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteRow(@RequestBody RedisDto redisDto) {
        int result = redisSingleDataService.deleteSingleData(redisDto.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
