package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "balance", indexes = {
    @Index(name = "balance_idx_user_id", columnList = "user_id")
})
public class Balance {

    private static final long MAX_BALANCE_AMOUNT = 10_000_000L;

    @Id
    @Column(name = "balance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private long amount;

    @Version
    private Integer version;

    private Balance(Long id, Long userId, long amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public static Balance of(Long userId, long amount) {
        validateAmount(amount);
        return new Balance(null, userId, amount);
    }

    public static Balance create(Long userId) {
        return of(userId, 0L);
    }

    public void charge(long amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }

        if (this.amount + amount > MAX_BALANCE_AMOUNT) {
            throw new IllegalArgumentException("충전 후 금액은 최대 금액을 초과할 수 없습니다.");
        }

        this.amount += amount;
    }

    public void use(long amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }

        if (this.amount - amount < 0) {
            throw new IllegalArgumentException("사용할 잔액이 부족합니다.");
        }

        this.amount -= amount;
    }

    private static void validateAmount(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }

        if (amount > MAX_BALANCE_AMOUNT) {
            throw new IllegalArgumentException("최대 금액을 초과할 수 없습니다.");
        }
    }
}
