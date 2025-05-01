package kr.hhplus.be.server.domain.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import kr.hhplus.be.server.domain.balance.BalanceCommand.Charge;
import kr.hhplus.be.server.domain.balance.BalanceCommand.Save;
import kr.hhplus.be.server.domain.balance.BalanceCommand.Use;
import kr.hhplus.be.server.domain.balance.BalanceTransactionInfo.Transactions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class BalanceServiceIntegrationTest {

    @Autowired
    private BalanceService balanceService;

    @Test
    @DisplayName("사용자 잔고가 있다면 충전 금액을 추가한다")
    void chargeBalance() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Charge command = Charge.of(1L, 10_000L);

        // when
        balanceService.chargeBalance(command);

        // then
        BalanceInfo.Balance result = balanceService.getBalance(1L);
        assertThat(result.getAmount()).isEqualTo(11_000L);
    }

    @Test
    @DisplayName("잔고 충전 금액이 0 이하이면 충전할 수 없다")
    void chargeBalanceNegativeAmount() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Charge command = Charge.of(1L, -1L);

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔고 충전 후 금액은 최대 금액 이하여야 한다.")
    void afterChargeBalanceUnderMaxAmount() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Charge command = Charge.of(1L, 10_000_000L);

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자 잔고 값이 없다면 0으로 새 잔고를 생성한다")
    void noUserBalanceThenCreateBalanceZero() {

        // given
        Charge command = Charge.of(1L, 1_000L);

        // when
        balanceService.chargeBalance(command);

        // then
        BalanceInfo.Balance balance = balanceService.getBalance(1L);
        assertThat(balance.getAmount()).isEqualTo(1_000L);
    }

    @Test
    @DisplayName("사용자 잔고 값 생성 시 최대 값 이하여야 한다")
    void noUserBalanceThenCreateBalanceBelowMaxAmount() {

        // given
        Charge command = Charge.of(1L, 10_000_001L);

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자 잔고를 사용한다")
    void useUserBalance() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Use command = Use.of(1L, 100L);

        // when
        balanceService.useBalance(command);

        // then
        BalanceInfo.Balance result = balanceService.getBalance(1L);
        assertThat(result.getAmount()).isEqualTo(900L);
    }

    @Test
    @DisplayName("사용 금액이 0 이하이면 잔고를 사용할 수 없다")
    void useUserBalanceAmountIsNegative() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Use command = Use.of(1L, -1L);

        // when, then
        assertThatThrownBy(() -> balanceService.useBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔고 사용 시 잔고는 사용할 금액보다 커야 한다.")
    void useUserBalanceNotEnough() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Use command = Use.of(1L, 1_001L);

        // when, then
        assertThatThrownBy(() -> balanceService.useBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔고 사용, 충전 시 그 내역을 저장한다")
    void saveBalanceAndBalanceTransaction() {

        // given, when
        Balance balance = Balance.of(10L, 1_000L);
        Save saveCommand = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommand);

        Charge chargeCommand = Charge.of(10L, 5_000L);
        balanceService.chargeBalance(chargeCommand);
        Use useCommand = Use.of(10L, 1_000L);
        balanceService.useBalance(useCommand);

        // then
        Transactions transactions = balanceService.findAllTransactions();
        assertThat(transactions.getTransactions()).hasSize(2)
            .extracting("transactionType", "amount")
            .containsExactly(
                tuple(BalanceTransactionType.CHARGE, 5_000L),
                tuple(BalanceTransactionType.USE, -1_000L)
            );
    }

    @Test
    @DisplayName("사용자의 잔고를 조회한다")
    void getUserBalance() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        Save saveCommnad = Save.of(balance.getUserId(), balance.getAmount());
        balanceService.saveBalance(saveCommnad);

        // when
        BalanceInfo.Balance result = balanceService.getBalance(1L);

        // then
        assertThat(result.getAmount()).isEqualTo(1_000L);
    }

    @Test
    @DisplayName("사용자 잔고 조회 시 값이 없다면 잔고를 0으로 반환한다")
    void getUserBalanceNotExist() {

        // when
        BalanceInfo.Balance result = balanceService.getBalance(1L);

        // then
        assertThat(result.getAmount()).isZero();
    }
}
