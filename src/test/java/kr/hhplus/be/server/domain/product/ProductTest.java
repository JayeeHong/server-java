package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kr.hhplus.be.server.domain.common.Price;
import kr.hhplus.be.server.domain.common.Stock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    private static final int MIN_PRICE = 0;
    private static final int MAX_PRICE = 10000000;
    private static final int MIN_STOCK = 0;
    private static final int MAX_STOCK = 1000;

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void 상품_생성시_금액이_0보다_작으면_유효성체크를_통과하지_못한다(int amount) {
        // when, then
        assertThatThrownBy(() -> new Product(1L, "productA", new Price(amount), new Stock(1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("최소 상품 금액은 " + MIN_PRICE + "입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10000001})
    void 상품_생성시_금액이_천만보다_크면_유효성체크를_통과하지_못한다(int amount) {
        // when, then
        assertThatThrownBy(() -> new Product(1L, "productA", new Price(amount), new Stock(1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("최대 상품 금액은 " + MAX_PRICE + "입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void 상품_생성시_재고가_0보다_작으면_유효성체크를_통과하지_못한다(int stock) {
        // when, then
        assertThatThrownBy(() -> new Product(1L, "productA", new Price(0), new Stock(stock)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("최소 재고는 " + MIN_STOCK + "개입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1001})
    void 상품_생성시_재고가_천보다_크면_유효성체크를_통과하지_못한다(int stock) {
        // when, then
        assertThatThrownBy(() -> new Product(1L, "productA", new Price(0), new Stock(stock)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("최대 재고는 " + MAX_STOCK + "개입니다.");
    }

}