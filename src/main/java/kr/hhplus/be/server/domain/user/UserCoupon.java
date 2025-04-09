package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
import lombok.Getter;

@Getter
public class UserCoupon {

    private final Long id;
    private final Long userId;
    private final Coupon coupon;
    private final LocalDateTime issuedAt;
    private boolean used;
    private LocalDateTime usedAt;

    public UserCoupon(Long id, Long userId, Coupon coupon, LocalDateTime issuedAt) {
        if (coupon == null) {
            throw new IllegalArgumentException("쿠폰은 null일 수 없습니다.");
        }
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.used = false;
        this.usedAt = null;
    }

    public void use() {
        if (isUsed()) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }
        if (coupon.isExpired()) {
            throw new IllegalStateException("만료된 쿠폰입니다.");
        }
        this.used = true;
        this.usedAt = LocalDateTime.now();
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isAvailable() {
        return !isUsed() && !coupon.isExpired();
    }

    public int applyDiscount(int price) {
        if (!isAvailable()) {
            throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
        }
        return coupon.applyDiscount(price);
    }

    public CouponResponse.UserCoupon translateUserCoupon() {
        return new CouponResponse.UserCoupon(userId, coupon.getCode().getValue(),
            coupon.getDiscount().getAmount(), coupon.getType(), used, LocalDateTime.now(),
            LocalDateTime.now());
    }

    public static List<CouponResponse.UserCoupon> translateUserCoupons(List<UserCoupon> userCoupons) {
        return userCoupons.stream()
            .map(UserCoupon::translateUserCoupon)
            .collect(Collectors.toList());
    }
}
