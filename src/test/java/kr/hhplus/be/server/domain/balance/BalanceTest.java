package kr.hhplus.be.server.domain.balance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    @DisplayName("잔액 생성 시 초기 값은 0 초과 10_000_000 이하여야 한다.")
    void isValidCreateBalanceTest() {
        // when, then
        assertThatThrownBy(() -> Balance.create(1L, -1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Balance.create(1L, 10_000_001L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔액 충전 시 금액은 0보다 커야 한다.")
    void isValidChargeAmountMinTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when, then
        assertThatThrownBy(() -> balance.charge(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔액 충전 시 충전 후 금액은 10_000_000 이하여야 한다.")
    void isValidChargeAmountMaxTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when, then
        assertThatThrownBy(() -> balance.charge(9_999_001L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔액 사용 시 사용 금액은 0보다 커야 한다.")
    void isValidUseAmountTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when, then
        assertThatThrownBy(() -> balance.use(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔액 사용 시 사용 후 금액은 0 이상이어야 한다.")
    void isValidAfterUseAmountTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when, then
        assertThatThrownBy(() -> balance.use(1_001L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔액 충전에 성공한다.")
    void chargeAmountSuccessTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when
        balance.charge(1_000L);

        // then
        assertEquals(2_000L, balance.getAmount());
        assertThat(balance.getBalanceTransactions()).hasSize(2)
            .extracting("amount").containsExactly(1_000L, 1_000L);
        assertThat(balance.getBalanceTransactions())
            .extracting("transactionType")
            .containsExactly(BalanceTransactionType.CHARGE, BalanceTransactionType.CHARGE);
    }

    @Test
    @DisplayName("잔액 사용에 성공한다.")
    void useAmountSuccessTest() {

        // given
        Balance balance = Balance.create(1L, 1_000L);

        // when
        balance.use(1_000L);

        // then
        assertEquals(0, balance.getAmount());
        assertThat(balance.getBalanceTransactions()).hasSize(2)
            .extracting("amount").containsExactly(1_000L, -1_000L);
        assertThat(balance.getBalanceTransactions())
            .extracting("transactionType").containsExactly(BalanceTransactionType.CHARGE, BalanceTransactionType.USE);
    }

}
