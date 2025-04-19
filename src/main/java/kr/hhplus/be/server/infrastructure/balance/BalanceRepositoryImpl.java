package kr.hhplus.be.server.infrastructure.balance;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {

    private final JpaBalanceRepository jpaBalanceRepository;

    @Override
    public Balance save(Balance balance) {
        return jpaBalanceRepository.save(balance);
    }

    @Override
    public List<Balance> findAllByUserId(Long userId) {
        return jpaBalanceRepository.findAllByUserId(userId);
    }

    @Override
    public int getTotalBalance(Long userId) {
        return jpaBalanceRepository.sumBalanceByUserId(userId);
    }
}
