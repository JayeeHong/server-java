package kr.hhplus.be.server.domain.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void 재고_정상_생성() {
        Stock stock = new Stock(10);
        assertEquals(10, stock.quantity());
    }

    @Test
    void 재고_음수_생성시_예외() {
        assertThrows(IllegalArgumentException.class, () -> new Stock(-1));
    }

    @Test
    void 재고_increase_정상_작동() {
        Stock stock = new Stock(5);
        Stock increased = stock.increase(3);

        assertEquals(8, increased.quantity());
    }

    @Test
    void 재고_decrease_정상_작동() {
        Stock stock = new Stock(10);
        Stock decreased = stock.decrease(4);

        assertEquals(6, decreased.quantity());
    }

    @Test
    void 재고_decrease_재고부족시_예외() {
        Stock stock = new Stock(2);
        assertThrows(IllegalArgumentException.class, () -> stock.decrease(5));
    }

    @Test
    void 재고_매진_확인() {
        Stock stock = new Stock(0);
        assertTrue(stock.isSoldOut());

        Stock stock2 = new Stock(3);
        assertFalse(stock2.isSoldOut());
    }

    @Test
    void 재고_동등성_확인() {
        Stock s1 = new Stock(10);
        Stock s2 = new Stock(10);
        Stock s3 = new Stock(5);

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
