
package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.user.*;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    BalanceRepository balanceRepository = mock(BalanceRepository.class);
    BalanceCalculator balanceCalculator = new BalanceCalculator(balanceRepository);

    UserService userService = new UserService(userRepository, balanceRepository, balanceCalculator);

    @Test
    @DisplayName("사용자가 금액을 충전하면 충전 이력이 저장되고 현재 잔액이 계산된다")
    void chargeBalance_success() {
        // given
        Long userId = 1L;
        int amount = 1000;
        User user = User.of(userId, "홍길동");

        when(userRepository.findOrThrow(userId)).thenReturn(user);
        when(balanceRepository.getTotalBalance(anyLong())).thenReturn(5000);

        // when
        UserResponse.Balance result = userService.chargeBalance(userId, amount);

        // then
        assertEquals(userId, result.getUserId());
        assertEquals("홍길동", result.getName());
        assertEquals(5000, result.getBalance());
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @Test
    @DisplayName("사용자의 현재 잔액을 조회할 수 있다")
    void getUserBalance_success() {
        // given
        Long userId = 1L;
        User user = User.of(userId, "홍길동");
        List<Balance> history = List.of(
            Balance.charge(userId, 3000),
            Balance.deduct(userId, 1000)
        );

        when(userRepository.findOrThrow(userId)).thenReturn(user);
        when(balanceRepository.getTotalBalance(anyLong())).thenReturn(4000);

        // when
        UserResponse.Balance result = userService.getUserBalance(userId);

        // then
        assertEquals(userId, result.getUserId());
        assertEquals("홍길동", result.getName());
        assertEquals(4000, result.getBalance());
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 잔액 충전 시 예외가 발생한다")
    void chargeBalance_invalidUser_throwsException() {
        // given
        Long invalidUserId = 999L;
        when(userRepository.findOrThrow(invalidUserId))
            .thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
            userService.chargeBalance(invalidUserId, 1000)
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 잔액 조회 시 예외가 발생한다")
    void getBalance_invalidUser_throwsException() {
        Long invalidUserId = 999L;
        when(userRepository.findOrThrow(invalidUserId))
            .thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

        assertThrows(IllegalArgumentException.class,
            () -> userService.getUserBalance(invalidUserId));
    }

}
