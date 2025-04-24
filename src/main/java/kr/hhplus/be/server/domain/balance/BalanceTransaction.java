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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Enumerated(EnumType.STRING)
    private BalanceTransactionType transactionType;

    private long amount;

    private BalanceTransaction(Long id,
                               Balance balance, BalanceTransactionType transactionType,
                               long amount) {
        this.id = id;
        this.balance = balance;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public static BalanceTransaction of(Balance balance, BalanceTransactionType transactionType, long amount) {
        return new BalanceTransaction(null, balance, transactionType, amount);
    }

    public static BalanceTransaction charge(Balance balance, long amount) {
        return of(balance, BalanceTransactionType.CHARGE, amount);
    }

    public static BalanceTransaction use(Balance balance, long amount) {
        return of(balance, BalanceTransactionType.USE, -amount);
    }
}
