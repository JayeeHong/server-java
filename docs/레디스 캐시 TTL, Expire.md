Redis 캐시에서 TTL(Time To Live) / Expire를 설정하는 방법과 그 적용 이유를 정리하면 다음과 같습니다.

---

## 1. TTL / Expire 개념

* **TTL(Time To Live)**: 키가 생성된 후 자동으로 삭제되기까지 남은 수명(초 단위).
* **Expire**: 특정 키에 TTL을 부여하는 명령. TTL이 0이 되면 키가 자동 삭제되어 메모리를 해제.

TTL을 두는 이유는 ‘언제까지’ 캐시된 데이터를 유효하다고 볼지 명확히 정의하기 위함입니다.

---

## 2. 적용 방법

### 2.1. Redis CLI

```bash
# 1) 키 생성
> SET user:123:name "홍길동"
OK

# 2) TTL 부여 (초 단위)
> EXPIRE user:123:name 300
(integer) 1

# 3) 남은 TTL 확인
> TTL user:123:name
(integer) 285

# 4) 만료시점 변경
> EXPIRE user:123:name 600   # 남은 시간을 600초로 연장
> PEXPIRE user:123:name 15000   # 밀리초 단위로 설정

# 5) TTL 제거 (무한 지속)
> PERSIST user:123:name
(integer) 1
```

### 2.2. Jedis (Java)

```java
Jedis jedis = new Jedis("localhost", 6379);
// 키 저장 + TTL 설정
jedis.setex("session:abc", 1800, jsonData);

// 또는 별도 설정
jedis.set("session:abc", jsonData);
jedis.expire("session:abc", 1800);
```

### 2.3. Spring Boot + Lettuce

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        // 기본 TTL 5분 설정
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .disableCachingNullValues();

        // 캐시별 TTL 개별 설정 (예: userCache는 10분)
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("userCache",
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(cf)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build();
    }
}
```

```java
@Service
public class UserService {
    @Cacheable(value = "userCache", key = "#userId")
    public UserDto getUser(Long userId) { … }
}
```

---

## 3. TTL / Expire 적용 이유

1. **메모리 관리**

    * Redis는 인메모리 DB이므로, 만료되지 않은 키가 계속 쌓이면 메모리 누수로 이어질 수 있음.
    * 불필요해진 캐시를 자동으로 정리해 유휴 메모리를 회수.

2. **데이터 신선도 유지**

    * 원본(예: DB)의 변경이 자주 일어날 때, 캐시에 오래된 데이터가 남아 있으면 일관성 문제가 발생.
    * TTL을 짧게 두면 최신 상태를 주기적으로 재조회하게 되어 캐시 오염 방지.

3. **장애 대응 및 장애 복구**

    * 장애 시 캐시에 남아 있던 잘못된 데이터가 계속 사용되는 것을 막고, 일정 시간 후 자동으로 초기화.

4. **캐시 스탬피드(Cache Stampede) 방지**

    * TTL 만료 시점이 몰릴 경우 동시에 많은 요청이 DB를 때리는 현상을 완화하기 위해,
    * 서로 다른 만료 시간을 랜덤하게 분산(예: `Duration.ofSeconds(300 + random(0,60))`) 적용.

---

## 4. 주의사항 및 베스트 프랙티스

* **적절한 TTL 길이 선택**

    * 너무 짧으면 캐시 히트율(재조회 비용)이 올라가고, 너무 길면 오래된 데이터가 유지됨.
* **Null 값 캐싱**

    * 조회 결과가 없을 때 `null` 또는 빈 객체도 캐싱할 경우, 짧은 TTL(예: 30초)로 설정해 “캐시 페널티” 완화.
* **캐시 무효화 전략**

    * TTL 외에 데이터 변경 시 애플리케이션 레벨에서 `evict` 또는 `delete`를 호출해 즉시 무효화.
* **모니터링**

    * `INFO memory`, `INFO stats` 명령어로 메모리 사용량, 만료된 키 수 등 모니터링.
---

## TTL이 누락되었을 때 발생할 수 있는 문제

1. 메모리 누수 및 과다 사용
* 키가 만료되지 않아 Redis 메모리에 불필요한 데이터가 계속 쌓이면서, 결국 메모리가 가득 차 OOM(Out Of Memory) 상황이 발생할 수 있습니다.

2. 데이터 신선도 저하
* 원본 데이터베이스에서 값이 변경되었더라도 캐시된 오래된 데이터가 갱신되지 않아, 사용자에게 stale(오래된) 데이터를 제공하게 됩니다.

3. 장애 복구 및 일관성 문제
* 잘못된 데이터가 영구적으로 남아 있으면 장애 발생 시 복구 과정에서 캐시에 남은 오류 데이터가 재사용되어 문제를 더 복잡하게 만들 수 있습니다.

4. 캐시 스탬피드 방지 불가능
* 만료 시점이 없기 때문에 캐시 갱신 시점이 분산되지 않아, 특정 시점에 동시에 많은 요청이 백엔드로 쏟아지는 캐시 스탬피드 현상을 막기 어렵습니다.

---

## 일간 랭킹에 TTL을 적용하지 않았을 때 생기는 문제

1. **메모리 누적**
* 매일 새로운 키(ranking:daily:YYYY-MM-DD)가 생성되지만 만료되지 않아 Redis 메모리에 쌓임 → OOM 리스크

2. 데이터 관리 복잡화
* 오래된 날짜의 랭킹을 일일이 수동으로 삭제·정리해야 함 → 운영 부담 증가

3. 스탠다드 운영 방해
* 오래된 일간 랭킹이 남아 있으면 조회 로직이 의도치 않게 과거 데이터까지 포함할 수 있음 → 비즈니스 로직 오류

4. 모니터링·모듈화 어려움
* TTL 기반 자동 만료가 없으면 “오늘” 랭킹만 따로 관리·모니터링하기 힘들어 집계 지표 신뢰도 저하

---

위와 같이 Redis 캐시에 TTL / Expire를 설정하면 메모리 효율을 높이고, 데이터 일관성 및 시스템 안정성을 확보할 수 있습니다.

