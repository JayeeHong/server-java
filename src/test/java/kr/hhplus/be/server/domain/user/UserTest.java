package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void 유저_생성_및_초기_잔고_확인() {
        User user = new User(1L, "홍길동", new Balance(10000));
        assertEquals(10000, user.getBalanceAmount());
        assertEquals("홍길동", user.getName());
    }

    @Test
    void 유저_잔고_충전_charge_정상작동() {
        User user = new User(1L, "홍길동", new Balance(10000));
        user.charge(5000);
        assertEquals(15000, user.getBalanceAmount());
    }

    @Test
    void 유저_잔고_차감_withdraw_정상작동() {
        User user = new User(1L, "홍길동", new Balance(10000));
        user.withdraw(4000);
        assertEquals(6000, user.getBalanceAmount());
    }

    @Test
    void 유저_잔고_차감_withdraw_부족시_예외() {
        User user = new User(1L, "홍길동", new Balance(3000));
        assertThrows(IllegalArgumentException.class, () -> user.withdraw(5000));
    }

    @Test
    void 유저_결제가능성_canAfford_검사() {
        User user = new User(1L, "홍길동", new Balance(7000));
        assertTrue(user.canAfford(5000));
        assertFalse(user.canAfford(8000));
    }
}