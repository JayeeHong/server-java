package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceTransaction {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long balanceId;

    @Enumerated(EnumType.STRING)
    private BalanceTransactionType transactionType;

    private long amount;

    private BalanceTransaction(Long id,
                               Long balanceId, BalanceTransactionType transactionType,
                               long amount) {
        this.id = id;
        this.balanceId = balanceId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public static BalanceTransaction of(Long balanceId, BalanceTransactionType transactionType, long amount) {
        return new BalanceTransaction(null, balanceId, transactionType, amount);
    }

    public static BalanceTransaction charge(Balance balance, long amount) {
        return of(balance.getId(), BalanceTransactionType.CHARGE, amount);
    }

    public static BalanceTransaction use(Balance balance, long amount) {
        return of(balance.getId(), BalanceTransactionType.USE, -amount);
    }
}
