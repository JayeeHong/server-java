package kr.hhplus.be.server.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @Column(name = "user_coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long couponId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coupon_id", nullable = false)
//    private Coupon coupon;

    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    private UserCoupon(Long id,
                       Long userId,
                       Long couponId,
                       LocalDateTime issuedAt,
                       LocalDateTime usedAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.issuedAt = issuedAt;
        this.usedAt = usedAt;
    }

    public static UserCoupon of(Long id, Long userId, Long couponId) {
        return new UserCoupon(id, userId, couponId, LocalDateTime.now(), null);
    }

    public static UserCoupon create(Long userId, Long couponId) {
        return UserCoupon.of(null, userId, couponId);
    }

    public void use() {
        if (cannotUse()) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }

        this.usedAt = LocalDateTime.now();
    }

    public boolean cannotUse() {
        return this.usedAt != null;
    }
}
