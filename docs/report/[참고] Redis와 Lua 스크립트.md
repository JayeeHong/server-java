#### [참고]
✅ Redis + Lua 스크립트란?
- Redis는 기본적으로 빠른 메모리 기반 데이터 저장소야.
- Redis는 단순한 GET/SET뿐 아니라, "여러 명령을 한 번에, 원자적으로(atomic) 처리할 수 있는" 방법을 제공해.
- 이때 사용하는 게 Lua 스크립트야.

#### Lua 스크립트란:
👉 "여러 Redis 명령을 묶어서 서버 쪽에서 한번에 실행하는 작은 프로그램"<br>
👉 즉, 클라이언트가 여러 번 왕복하지 않고, 서버에서 한 번에 '딱' 실행돼.

✅ 왜 Lua 스크립트를 써?

선착순 쿠폰 발급 문제에서
사용자들이 초당 수천 번 "쿠폰 주세요!" 요청을 보내면:
- DB는 락 경합(lock contention) 때문에 터질 수 있어.
- Redis에서만 빠르게 "남은 쿠폰 수"를 줄이고, 초과 발급을 막고 싶어.
- 그런데 Redis에서도 DECR 같은 명령을 따로따로 보내면, 그 순간에도 경합이 생길 수 있어.

그래서:
> "쿠폰 수량 체크 + 감소"를 하나의 트랜잭션처럼 묶어서 Redis 서버 한방에 처리하는 거야.

이걸 Lua 스크립트로 작성하는 거야.

✅ 예시: 쿠폰 선착순 발급 Lua 스크립트

```lua
-- Lua 스크립트 내용
local remain = tonumber(redis.call('GET', KEYS[1]))
if remain > 0 then
    redis.call('DECR', KEYS[1])
return 1 -- 성공
else
    return 0 -- 실패 (쿠폰 소진)
end
```

#### → 한 번에:
1. 현재 남은 수량 읽고
2. 남아있으면 감소시키고
3. 결과를 반환함
#### → 원자적(Atomic) 실행! 중간에 다른 요청이 끼어들 수 없음.

✅ Java(Spring)에서 호출 예시

```java
String luaScript =
    "local remain = tonumber(redis.call('GET', KEYS[1])) " +
    "if remain > 0 then " +
    "  redis.call('DECR', KEYS[1]) " +
    "  return 1 " +
    "else " +
    "  return 0 " +
    "end";

DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
redisScript.setScriptText(luaScript);
redisScript.setResultType(Long.class);

Long result = redisTemplate.execute(redisScript, List.of("coupon:10001"));
if (result == 1L) {
    System.out.println("쿠폰 발급 성공!");
} else {
    System.out.println("쿠폰 소진!");
}
```
✅ 요약

- Redis + Lua 스크립트 = 초고속 선착순 처리용 기술
- 한번에 쿠폰 남은 수량 체크 + 감소를 원자적으로 실행
- DB 락 병목 없이 고성능 처리 가능

한줄 요약
> Lua 스크립트는 "Redis 서버 안에서 미니 프로그램처럼" 여러 명령을 한 번에 묶어 원자적으로 처리하는 기술이다. 선착순 쿠폰, 선착순 좌석 예약, 선착순 주문 등에 자주 쓴다.


