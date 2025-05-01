package kr.hhplus.be.server.domain.balance;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceTransactionInfo {

    @Getter
    public static class Transactions {

        private final List<Transaction> transactions;

        private Transactions(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        public static Transactions of(List<Transaction> transactions) {
            return new Transactions(transactions);
        }
    }

    @Getter
    public static class Transaction {

        private final long id;
        private final long balanceId;
        private final BalanceTransactionType transactionType;
        private final long amount;

        private Transaction(long id, long balanceId, BalanceTransactionType transactionType, long amount) {
            this.id = id;
            this.balanceId = balanceId;
            this.transactionType = transactionType;
            this.amount = amount;
        }

        public static Transaction of(long id, long balanceId, BalanceTransactionType transactionType, long amount) {
            return new Transaction(id, balanceId, transactionType, amount);
        }

        public static Transaction toTransactionInfo(BalanceTransaction balanceTransaction) {
            return Transaction.of(
                balanceTransaction.getId(),
                balanceTransaction.getBalanceId(),
                balanceTransaction.getTransactionType(),
                balanceTransaction.getAmount()
            );
        }
    }
}
