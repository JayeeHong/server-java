package kr.hhplus.be.server.infrastructure.balance;

import java.util.List;
import kr.hhplus.be.server.domain.user.Balance;

public interface BalanceRepository {

    /**
     * 사용자 잔액 이력 저장
     */
    void save(Balance balance);

    /**
     * 사용자 잔액 이력 전체 조회
     */
    List<Balance> findAllByUserId(Long userId);

}
