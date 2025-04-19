package kr.hhplus.be.server.domain.balance;

import java.util.List;

public interface BalanceRepository {

    /**
     * 사용자 잔액 이력 저장
     */
    Balance save(Balance balance);

    /**
     * 사용자 잔액 이력 전체 조회
     */
    List<Balance> findAllByUserId(Long userId);

    /**
     * 사용자의 총 잔액 계산
     */
    int getTotalBalance(Long userId);
}
