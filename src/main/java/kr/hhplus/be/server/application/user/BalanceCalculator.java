package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceCalculator {

    private final BalanceRepository balanceRepository;

    public int calculate(Long userId) {
        return balanceRepository.getTotalBalance(userId);
    }

}
