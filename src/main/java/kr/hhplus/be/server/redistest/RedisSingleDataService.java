package kr.hhplus.be.server.redistest;

import java.time.Duration;
import org.springframework.stereotype.Service;

/**
 * Redis 단일 데이터를 처리하는 비즈니스 로직 인터페이스
 */
@Service
public interface RedisSingleDataService {

    // Redis 단일 데이터 값 등록/수정
    int setSingleData(String key, Object value);

    // Redis 단일 데이터 값 등록/수정 (duration 값이 존재하면 메모리 상 유효시간 지정
    int setSingleData(String key, Object value, Duration duration);

    // Redis 키 기반 단일 데이터 값 조회
    String getSingleData(String key);

    // Redis 키 기반 단일 데이터 값 삭제
    int deleteSingleData(String key);
    
}
