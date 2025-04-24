package kr.hhplus.be.server.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("사용자가 금액을 충전하면 CHARGE 트랜잭션 생성")
    void chargeBalance_createsChargeTransaction() {
        User user = User.of(1L, "홍길동");
        Balance balance = Balance.charge(user.getId(), 1000);

        assertEquals(1L, balance.userId());
        assertEquals(1000, balance.amount());
        assertEquals(TransactionType.CHARGE, balance.transactionType());
        assertNotNull(balance.createdAt());
    }

    @Test
    @DisplayName("사용자가 금액을 차감하면 PAYMENT 트랜잭션 생성")
    void deductBalance_createsPaymentTransaction() {
        User user = User.of(1L, "홍길동");
        Balance balance = Balance.deduct(user.getId(), 500);

        assertEquals(1L, balance.userId());
        assertEquals(-500, balance.amount());
        assertEquals(TransactionType.PAYMENT, balance.transactionType());
    }

    @Test
    @DisplayName("사용자는 잔액 이력을 기반으로 총 잔액을 계산할 수 있다")
    void calculateBalance_sumsAllTransactionsCorrectly() {
        User user = User.of(1L, "홍길동");
        List<Balance> histories = List.of(
            Balance.charge(1L, 1000),
            Balance.deduct(1L, 200),
            Balance.charge(1L, 300)
        );

        int result = user.calculateBalance(histories);
        assertEquals(1100, result);
    }
}