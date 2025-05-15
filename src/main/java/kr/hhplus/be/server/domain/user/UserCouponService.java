package kr.hhplus.be.server.domain.user;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
        double score = System.currentTimeMillis();

        // 이미 발급된 사용자인지 확인
        Boolean alreadyIssued = redisTemplate.opsForZSet().score(key, String.valueOf(command.getUserId())) != null;
        if (Boolean.TRUE.equals(alreadyIssued)) {
            log.info("이미 사용자에게 발급된 쿠폰입니다. couponId={}, userId={}", command.getCouponId(),
                command.getUserId());
            throw new IllegalStateException("이미 사용자에게 발급된 쿠폰입니다.");
        }

        // 레디스 sorted set에 (userId, timestamp) 추가
        redisTemplate.opsForZSet().add(key, String.valueOf(command.getUserId()), score);

        // 순위 확인
        Long rank = redisTemplate.opsForZSet().rank(key, String.valueOf(command.getUserId()));
        if (rank == null || rank >= limit) {
            // N명 이후라면 방금 추가한 엔트리 제거 후 에러
            redisTemplate.opsForZSet().remove(key, String.valueOf(command.getUserId()));
            log.info("선착순 발급 수량 초과");
            throw new IllegalStateException("선착순 발급 수량 초과");
        }

        // 쿠폰의 만료일에 맞춰 TTL 지정
        redisTemplate.expireAt(key, Date.from(
            couponRepository.findById(command.getCouponId()).getExpiredAt()
                .atZone(ZoneId.of("Asia/Seoul")).toInstant()
        ));
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
