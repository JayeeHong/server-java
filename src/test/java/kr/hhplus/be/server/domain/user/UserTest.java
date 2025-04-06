package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void 사용자_생성시_잔액이_0보다_작으면_유효성체크를_통과하지_못한다(int balance) {
        // when, then
        assertThatThrownBy(() -> new User(1L, "userA", balance))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("잔액이 부족합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1000001})
    void 사용자_생성시_잔액이_100만원_초과이면_유효성체크를_통과하지_못한다(int balance) {
        // when, then
        assertThatThrownBy(() -> new User(1L, "userA", balance))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("최대 잔액은 1,000,000원입니다.");
    }

}