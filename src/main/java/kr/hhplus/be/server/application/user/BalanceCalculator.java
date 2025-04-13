package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceCalculator {

    private final BalanceRepository balanceRepository;

    public static int calculate(List<Balance> history) {
        return history.stream()
            .mapToInt(balance ->
                balance.transactionType().isCharge()
                    ? balance.amount()
                    : -balance.amount()
            )
            .sum();
    }

}
