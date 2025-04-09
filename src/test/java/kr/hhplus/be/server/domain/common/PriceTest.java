package kr.hhplus.be.server.domain.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void 가격_정상_생성() {
        Price price = new Price(10000);
        assertEquals(10000, price.value());
    }

    @Test
    void 가격_음수_생성시_예외() {
        assertThrows(IllegalArgumentException.class, () -> new Price(-100));
    }

    @Test
    void 가격_add_정상_작동() {
        Price p1 = new Price(10000);
        Price p2 = new Price(5000);
        Price result = p1.add(p2);

        assertEquals(15000, result.value());
    }

    @Test
    void 가격_subtract_정상_작동() {
        Price p1 = new Price(10000);
        Price p2 = new Price(3000);
        Price result = p1.subtract(p2);

        assertEquals(7000, result.value());
    }

    @Test
    void 가격_subtract_부족하면_예외() {
        Price p1 = new Price(3000);
        Price p2 = new Price(5000);

        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p2));
    }

    @Test
    void 가격_동등성_확인() {
        Price p1 = new Price(10000);
        Price p2 = new Price(10000);
        Price p3 = new Price(5000);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
