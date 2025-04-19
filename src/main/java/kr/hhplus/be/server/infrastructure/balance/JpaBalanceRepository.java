package kr.hhplus.be.server.infrastructure.balance;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaBalanceRepository extends JpaRepository<Balance, Long> {

    List<Balance> findAllByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Balance b WHERE b.userId = :userId")
    int sumBalanceByUserId(@Param("userId") Long userId);

}
