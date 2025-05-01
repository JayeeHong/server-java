package kr.hhplus.be.server.domain.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.hhplus.be.server.domain.balance.BalanceCommand.Charge;
import kr.hhplus.be.server.domain.balance.BalanceCommand.Use;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BalanceServiceUnitTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private BalanceRepository balanceRepository;

    @Test
    @DisplayName("잔고 충전 금액은 0보다 커야 한다")
    void chargeBalancePositiveAmount() {

        // given
        Charge command = Charge.of(1L, 0L);
        Balance balance = Balance.of(command.getUserId(), command.getAmount());

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.of(balance));

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔고 충전은 최대 금액을 넘길 수 없다")
    void chargeBalanceCannotExceedMaxAmount() {

        // given
        Charge command = Charge.of(1L, 10_000_000L);
        Balance balance = Balance.of(command.getUserId(), command.getAmount());

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.of(balance));

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잔고 충전 시 사용자 잔고가 없으면 잔고를 생성하고 잔고를 충전한다")
    void notExistBalanceThenCreateBalance() {

        // given
        Charge command = Charge.of(1L, 1_000L);
        Balance balance = Balance.create(command.getUserId());

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.empty());

        when(balanceRepository.save(any(Balance.class)))
            .thenReturn(balance);

        // when
        balanceService.chargeBalance(command);

        // then
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @Test
    @DisplayName("사용자 잔고를 충전한다")
    void chargeBalance() {

        // given
        BalanceCommand.Charge command = mock(BalanceCommand.Charge.class);
        Balance balance = mock(Balance.class);

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.of(balance));

        // when
        balanceService.chargeBalance(command);

        // then
        verify(balance, times(1)).charge(command.getAmount());
        verify(balanceRepository, times(0)).save(any(Balance.class));
    }

    @Test
    @DisplayName("잔고가 없으면 잔고를 사용하지 못한다")
    void balanceNotExistCannotUse() {

        // given
        Use command = Use.of(1L, 10_000L);

        Balance balance = Balance.create(command.getUserId());

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.empty());

        when(balanceRepository.save(any(Balance.class)))
            .thenReturn(balance);

        // when, then
        assertThatThrownBy(() -> balanceService.useBalance(command))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자의 잔고를 사용한다")
    void useBalance() {

        // given
        Use command = Use.of(1L, 1_000L);
        Balance balance = Balance.of(1L, 1_000L);

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.of(balance));

        // when
        balanceService.useBalance(command);

        // then
        assertThat(balance.getAmount()).isZero();
    }

    @Test
    @DisplayName("잔고 조회 시 없으면 0을 반환한다")
    void balanceNotExistReturnZero() {

        // given
        when(balanceRepository.findOptionalByUserId(1L))
            .thenReturn(Optional.empty());

        // when
        BalanceInfo.Balance balanceInfo = balanceService.getBalance(1L);

        // then
        assertThat(balanceInfo.getAmount()).isZero();
    }

    @Test
    @DisplayName("사용자의 잔고를 조회한다")
    void getBalance() {

        // given
        Balance balance = Balance.of(1L, 1_000L);

        when(balanceRepository.findOptionalByUserId(anyLong()))
            .thenReturn(Optional.of(balance));

        // when
        BalanceInfo.Balance balanceInfo = balanceService.getBalance(1L);

        // then
        assertThat(balanceInfo.getAmount()).isEqualTo(1_000L);
    }
}
