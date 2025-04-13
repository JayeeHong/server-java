package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    void 잔고_정상_생성() {
        Balance balance = new Balance(1000);
        assertEquals(1000, balance.amount());
    }

    @Test
    void 잔고_음수_생성시_예외() {
        assertThrows(IllegalArgumentException.class, () -> new Balance(-1));
    }

    @Test
    void 잔고_충전_add_정상() {
        Balance balance = new Balance(1000);
        Balance added = balance.add(500);
        assertEquals(1500, added.amount());
    }

    @Test
    void 잔고_차감_subtract_정상() {
        Balance balance = new Balance(1000);
        Balance subtracted = balance.subtract(400);
        assertEquals(600, subtracted.amount());
    }

    @Test
    void 잔고_차감시_잔고부족_예외() {
        Balance balance = new Balance(500);
        assertThrows(IllegalArgumentException.class, () -> balance.subtract(600));
    }

    @Test
    void 잔고_isEnough_확인() {
        Balance balance = new Balance(1000);
        assertTrue(balance.isEnough(500));
        assertFalse(balance.isEnough(1500));
    }

    @Test
    void 잔고_동등성_확인() {
        Balance b1 = new Balance(1000);
        Balance b2 = new Balance(1000);
        Balance b3 = new Balance(500);

        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertEquals(b1.hashCode(), b2.hashCode());
    }
}
