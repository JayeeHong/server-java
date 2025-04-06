package kr.hhplus.be.server.domain.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AmountTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 금액이_0보다_작으면_유효성체크를_통과하지_못한다(int amount) {
        // when, then
        assertThatThrownBy(() -> Amount.of(amount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("금액은 0보다 커야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1})
    void 금액이_0보다_크면_유효성체크를_통과한다(int amount) {
        // when, then
        assertThat(Amount.of(amount).getValue()).isEqualTo(amount);
    }

}