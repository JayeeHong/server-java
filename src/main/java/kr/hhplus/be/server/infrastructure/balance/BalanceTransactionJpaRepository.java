package kr.hhplus.be.server.infrastructure.balance;

import kr.hhplus.be.server.domain.balance.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionJpaRepository extends JpaRepository<BalanceTransaction, Long> {

}
