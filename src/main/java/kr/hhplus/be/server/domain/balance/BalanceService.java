package kr.hhplus.be.server.domain.balance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    // 사용자의 잔액을 충전한다
    @Transactional
    public void chargeBalance(BalanceCommand.Charge command) {
        Balance balance = balanceRepository.findOptionalByUserId(command.getUserId())
            .orElseGet(() -> balanceRepository.save(Balance.create(command.getUserId(), 0L)));

        balance.charge(command.getAmount());

        BalanceTransaction transaction = BalanceTransaction.charge(balance, command.getAmount());
        balanceRepository.saveTransaction(transaction);
    }

    // 사용자의 잔액을 사용한다
    @Transactional
    public void useBalance(BalanceCommand.Use command) {
        Balance balance = balanceRepository.findOptionalByUserId(command.getUserId())
            .orElseGet(() -> balanceRepository.save(Balance.create(command.getUserId(), 0L)));

        balance.use(command.getAmount());

        BalanceTransaction transaction = BalanceTransaction.use(balance, command.getAmount());
        balanceRepository.saveTransaction(transaction);
    }

    // 사용자의 잔액을 조회한다
    public BalanceInfo.Balance getBalance(Long userId) {
        Long amount = balanceRepository.findOptionalByUserId(userId)
            .map(Balance::getAmount)
            .orElse(0L);

        return BalanceInfo.Balance.of(amount);
    }
}
