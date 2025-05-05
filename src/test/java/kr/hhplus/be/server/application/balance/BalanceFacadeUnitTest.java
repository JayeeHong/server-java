package kr.hhplus.be.server.application.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.application.balance.BalanceCriteria.Charge;
import kr.hhplus.be.server.application.balance.BalanceResult.Balance;
import kr.hhplus.be.server.domain.balance.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BalanceFacadeUnitTest {

    @InjectMocks
    private BalanceFacade balanceFacade;

    @Mock
    private BalanceService balanceService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("잔액을 충전한다")
    void chargeBalance() {

        // given
        BalanceCriteria.Charge criteria = mock(BalanceCriteria.Charge.class);

        // when
        balanceFacade.chargeBalance(criteria);

        // then
        InOrder inOrder = inOrder(userService, balanceService);
        inOrder.verify(userService, times(1)).getUser(criteria.getUserId());
        inOrder.verify(balanceService, times(1)).chargeBalance(criteria.toCommand());
    }

    @Test
    @DisplayName("잔액을 조회한다")
    void getBalance() {

        // given
        when(balanceService.getBalance(anyLong()))
            .thenReturn(BalanceInfo.Balance.of(10_000L));

        // when
        BalanceResult.Balance balance = balanceFacade.getBalance(anyLong());

        // then
        InOrder inOrder = inOrder(userService, balanceService);
        inOrder.verify(userService, times(1)).getUser(anyLong());
        inOrder.verify(balanceService, times(1)).getBalance(anyLong());
        assertThat(balance.getAmount()).isEqualTo(10_000L);
    }
}