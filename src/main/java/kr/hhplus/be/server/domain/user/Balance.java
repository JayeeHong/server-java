package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Balance {

    private final Long userId;
    private final int amount;
    private final TransactionType transactionType;
    private final LocalDateTime createdAt;

    private Balance(Long userId, int amount, TransactionType transactionType) {
        this.userId = userId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = LocalDateTime.now();
    }

    public static Balance charge(Long userId, int amount) {
        return new Balance(userId, amount, TransactionType.CHARGE);
    }

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
