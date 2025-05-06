package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceCriteria {

    @Getter
    public static class Charge {

        private final Long userId;
        private final Long amount;

        private Charge(Long userId, Long amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public static Charge of(Long userId, Long amount) {
            return new Charge(userId, amount);
        }

        public BalanceCommand.Charge toCommand() {
            return BalanceCommand.Charge.of(userId, amount);
        }
    }

    @Getter
    public static class Use {
        private final Long userId;
        private final Long amount;

        private Use(Long userId, Long amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public static Use of(Long userId, Long amount) {
            return new Use(userId, amount);
        }

        public BalanceCommand.Use toCommand() {
            return BalanceCommand.Use.of(userId, amount);
        }
    }
}
