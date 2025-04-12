package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.Coupon;

public class UserCoupon {

    private final Long id;
    private final Long userId;
    private final Coupon coupon;
    private final LocalDateTime issuedAt;

    private UserCoupon(Long id, Long userId, Coupon coupon, LocalDateTime issuedAt) {
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon issue(Long userId, Coupon coupon, LocalDateTime now) {
        return new UserCoupon(null, userId, coupon, now);
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Coupon coupon() {
        return coupon;
    }

    public LocalDateTime issuedAt() {
        return issuedAt;
    }
}
