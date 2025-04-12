package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.domain.user.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceCalculator {

    private final BalanceRepository balanceRepository;

    public int calculate(User user) {
        List<Balance> histories = balanceRepository.findAllByUserId(user.id());
        return user.calculateBalance(histories);
    }
}
