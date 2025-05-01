package kr.hhplus.be.server.infrastructure.balance;

import java.util.Optional;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.balance.BalanceTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {

    private BalanceJpaRepository balanceJpaRepository;
    private BalanceTransactionJpaRepository balanceTransactionJpaRepository;

    @Override
    public Optional<Balance> findOptionalByUserId(Long userId) {
        return balanceJpaRepository.findByUserId(userId);
    }

    @Override
    public Balance save(Balance balance) {
        return balanceJpaRepository.save(balance);
    }

    @Override
    public BalanceTransaction saveTransaction(BalanceTransaction balanceTransaction) {
        return balanceTransactionJpaRepository.save(balanceTransaction);
    }
}
