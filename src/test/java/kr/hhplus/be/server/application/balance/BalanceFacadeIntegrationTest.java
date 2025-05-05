package kr.hhplus.be.server.application.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceInfo.Balance;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.user.UserCommand.Create;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class BalanceFacadeIntegrationTest {

    @Autowired
    private BalanceFacade balanceFacade;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserService userService;

    UserInfo.User user;

    BalanceCommand.Save balance;

    @BeforeEach
    void setUp() {
        user = userService.createUser(Create.of("userA"));

        balance = BalanceCommand.Save.of(user.getUserId(), 1_000_000L);
        balanceService.saveBalance(balance);
    }

    @Test
    @DisplayName("잔액을 충전한다")
    void chargeBalance() {

        // given
        BalanceCriteria.Charge criteria = BalanceCriteria.Charge.of(user.getUserId(), 10_000L);

        // when
        balanceFacade.chargeBalance(criteria);

        Balance findBalance = balanceService.getBalance(user.getUserId());
        assertThat(findBalance.getAmount()).isEqualTo(1_010_000L);
    }

    @Test
    @DisplayName("잔액을 조회한다")
    void getBalance() {

        // given
        Long userId = user.getUserId();

        // when
        BalanceResult.Balance findBalance = balanceFacade.getBalance(userId);

        // then
        assertThat(findBalance.getAmount()).isEqualTo(1_000_000L);
    }
}
