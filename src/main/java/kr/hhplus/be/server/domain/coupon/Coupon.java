
package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int discountAmount;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private LocalDateTime expiredAt;

    private Coupon(Long id, String name, int discountAmount, int quantity, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.status = CouponStatus.REGISTERED;
        this.expiredAt = expiredAt;
    }

    public static Coupon of(Long id, String name, int discountAmount, int quantity, LocalDateTime expiredAt) {
        return new Coupon(id, name, discountAmount, quantity, expiredAt);
    }

    public static Coupon create(String name,
                                int discountAmount,
                                int quantity,
                                LocalDateTime expiredAt) {
        validateName(name);
        validateDiscountAmount(discountAmount);
        validateQuantity(quantity);
        validateExpiredAt(expiredAt);

        return Coupon.of(null, name, discountAmount, quantity, expiredAt);
    }

    // 쿠폰 발급
    public Coupon issue() {

        if (this.status.cannotPublishable()) {
            throw new IllegalStateException("쿠폰을 발급할 수 없습니다.");
        }

        if (this.expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("쿠폰이 만료되었습니다.");
        }

        if (this.quantity <= 0) {
            throw new IllegalStateException("발급할 쿠폰 수량이 부족합니다.");
        }

        this.quantity -= 1;

        return this;
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("쿠폰 이름은 필수입니다.");
        }
    }

    private static void validateDiscountAmount(int discountAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("할인 금액은 0보다 작을 수 없습니다.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("쿠폰 수량은 0보다 커야 합니다.");
        }
    }

    private static void validateExpiredAt(LocalDateTime expiredAt) {
        if (expiredAt == null) {
            throw new IllegalArgumentException("쿠폰 만료일은 필수입니다.");
        }

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("쿠폰 만료일은 현재 시간 이후여야 합니다.");
        }
    }
}
