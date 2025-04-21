
package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int discountAmount;

    private int stock;

    private boolean active;

    private LocalDateTime expiredAt;

    @Version
    private Long version;

    private Coupon(Long id, String name, int discountAmount, int stock, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.stock = stock;
        this.active = true;
        this.expiredAt = expiredAt;
    }

    public static Coupon of(Long id, String name, int discountAmount, int stock, LocalDateTime expiredAt) {
        return new Coupon(id, name, discountAmount, stock, expiredAt);
    }

    public Coupon issue() {
        if (stock <= 0) {
            throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        }
        return new Coupon(id, name, discountAmount, stock - 1, LocalDateTime.now());
    }

    public boolean isExpired() {
        return !active || expiredAt.isBefore(LocalDateTime.now());
    }
}
