package kr.hhplus.be.server.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    private UserCoupon(Long id, Long userId, Coupon coupon, LocalDateTime issuedAt) {
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.usedAt = null;
    }

    public static UserCoupon issue(Long userId, Coupon coupon, LocalDateTime now) {
        return new UserCoupon(null, userId, coupon, now);
    }

    public void markAsUsed() {
        if (this.usedAt != null) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }

        this.usedAt = LocalDateTime.now();
    }
}
