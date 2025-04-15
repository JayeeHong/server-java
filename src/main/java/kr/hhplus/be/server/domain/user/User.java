package kr.hhplus.be.server.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User of(Long id, String name) {
        return new User(id, name);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    /**
     * 이력 기준 총 잔액 계산
     */
    public int calculateBalance(List<Balance> histories) {
        return histories.stream()
            .mapToInt(Balance::amount)
            .sum();
    }

    public void useBalance(int amount) {
        if (amount < 0) throw new IllegalArgumentException("차감 금액이 유효하지 않습니다.");
        // 실제 구현은 Balance 이력 생성 + 검증 로직 포함 가능
    }
}
