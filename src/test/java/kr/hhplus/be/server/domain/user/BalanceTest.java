package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    @DisplayName("충전 이력은 CHARGE 타입으로 생성된다")
    void createChargeBalance_success() {
        Balance balance = Balance.charge(1L, 1000);
        assertEquals(1L, balance.userId());
        assertEquals(1000, balance.amount());
        assertEquals(TransactionType.CHARGE, balance.transactionType());
    }

    @Test
    @DisplayName("차감 이력은 PAYMENT 타입으로 생성된다")
    void createDeductBalance_success() {
        Balance balance = Balance.deduct(2L, 500);
        assertEquals(2L, balance.userId());
        assertEquals(-500, balance.amount());
        assertEquals(TransactionType.PAYMENT, balance.transactionType());
    }
}
