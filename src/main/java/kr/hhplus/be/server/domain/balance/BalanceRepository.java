package kr.hhplus.be.server.domain.balance;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface BalanceRepository {

    Optional<Balance> findOptionalByUserId(Long userId);

    Balance save(Balance balance);

    BalanceTransaction saveTransaction(BalanceTransaction balanceTransaction);

    List<BalanceTransaction> findAllTransacitons();
}
