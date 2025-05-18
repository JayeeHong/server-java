package kr.hhplus.be.server.domain.user;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ISSUED_KEY_PREFIX = "usercoupon:issued:";

    @Value("${coupon.issue.limit}")
    private int limit; //선착순 허용 인원

    // 사용자 쿠폰을 발행한다
    public UserCouponInfo.Coupon createUserCoupon(UserCouponCommand.Publish command) {
        String key = ISSUED_KEY_PREFIX + command.getCouponId();

        try {
            UserCoupon findCoupon = userCouponRepository.findByUserIdAndCouponId(
                command.getUserId(),
                command.getCouponId());

            if (findCoupon != null) {
                throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
            }

            UserCoupon userCoupon = UserCoupon.create(command.getUserId(), command.getCouponId());
            userCouponRepository.save(userCoupon);

            return UserCouponInfo.Coupon.toUserCouponInfo(userCoupon);
        } catch (RuntimeException e) {
            // DB 저장 실패 시 레디스 예약도 취소
            redisTemplate.opsForZSet().remove(key, String.valueOf(command.getUserId()));
            throw e;
        }
    }

    // 사용자 쿠폰 발행 - 레디스
    public void createUserCouponRedis(UserCouponCommand.Publish command) {
        String key = ISSUED_KEY_PREFIX + command.getCouponId();
        String userId = String.valueOf(command.getUserId());
        double score = System.currentTimeMillis();

        // 쿠폰 만료일을 epoch seconds 로
        long expireAt = couponRepository.findById(command.getCouponId())
            .getExpiredAt()
            .atZone(ZoneId.of("Asia/Seoul"))
            .toEpochSecond();

        boolean success = false;
        int attempts = 0;

        while (!success && attempts < 5) {
            attempts++;

            List<Object> txResults = redisTemplate.execute(new SessionCallback<>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<Object> execute(RedisOperations ops) throws DataAccessException {
                    // 1) 변경 감시 시작
                    ops.watch(key);

                    // 2) 이미 발급된 사용자인지 검사
                    if (ops.opsForZSet().score(key, userId) != null) {
                        ops.unwatch();
                        throw new IllegalStateException("이미 발급된 쿠폰입니다.");
                    }

                    // 3) 트랜잭션 시작
                    ops.multi();

                    // 4) ZADD
                    ops.opsForZSet().add(key, userId, score);
                    // 5) RANK 조회 (0-base)
                    ops.opsForZSet().rank(key, userId);
                    // 6) EXPIREAT 설정
                    ops.expireAt(key, Date.from(Instant.ofEpochSecond(expireAt)));

                    // 7) EXEC
                    return ops.exec();
                }
            });

            if (txResults == null) {
                // 다른 클라이언트에 의해 WATCH 키가 변경되어 EXEC 이 취소됨 → 재시도
                continue;
            }

            // txResults = [ Long(addedCount), Long(rank), Boolean(expireResult) ]
            Long rank = (Long) txResults.get(1);
            if (rank == null || rank >= limit) {
                // 제한 초과 시 롤백: ZREM
                redisTemplate.opsForZSet().remove(key, userId);
                throw new IllegalStateException("선착순 발급 수량 초과");
            }

            success = true;  // 정상 처리
            log.debug("쿠폰 발급 성공 (rank={}%)", rank);
        }

        if (!success) {
            throw new IllegalStateException("Redis 트랜잭션이 반복적으로 충돌하여 발급에 실패했습니다.");
        }
    }

    // 사용할 수 있는 쿠폰을 조회한다
    public UserCouponInfo.UsableCoupon getUsableCoupon(UserCouponCommand.UsableCoupon command) {
        UserCoupon findCoupon = userCouponRepository.findByUserIdAndCouponId(command.getUserId(),
            command.getCouponId());

        if (findCoupon == null) {
            return null;
        }

        if (findCoupon.cannotUse()) {
            throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
        }

        return UserCouponInfo.UsableCoupon.of(findCoupon.getId());
    }

    // 쿠폰을 사용한다
    public void useUserCoupon(Long userCouponId) {
        UserCoupon findCoupon = userCouponRepository.findById(userCouponId);
        findCoupon.use();
    }

    // 사용자의 쿠폰 목록을 조회한다
    public UserCouponInfo.Coupons getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndUsable(userId);

        return UserCouponInfo.Coupons.of(userCoupons.stream()
            .map(UserCouponInfo.Coupon::toUserCouponInfo)
            .toList());
    }

    // 사용자 쿠폰 ID로 쿠폰을 조회한다
    public UserCouponInfo.Coupon getUserCouponById(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId);

        return UserCouponInfo.Coupon.toUserCouponInfo(userCoupon);
    }

    // 사용자 ID와 쿠폰 ID로 쿠폰을 조회한다
    public UserCouponInfo.Coupon getUserCoupon(Long userId, Long couponId) {
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId);

        return UserCouponInfo.Coupon.toUserCouponInfo(userCoupon);
    }
}
