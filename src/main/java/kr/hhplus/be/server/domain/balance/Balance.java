package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
@Table(name = "balances")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    // JPA 기본 생성자
    protected Balance() {}

    private Balance(Long userId, int amount, TransactionType transactionType) {
        this.userId = userId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
    }

    // 잔액 충전
    public static Balance charge(Long userId, int amount) {
        return new Balance(userId, amount, TransactionType.CHARGE);
    }

    // 잔액 차감
    public static Balance deduct(Long userId, int amount) {
        return new Balance(userId, -amount, TransactionType.PAYMENT);
    }

    public Long userId() {
        return userId;
    }

    public int amount() {
        return amount;
    }

    public TransactionType transactionType() {
        return transactionType;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

}
