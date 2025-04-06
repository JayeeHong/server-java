package kr.hhplus.be.server.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final long USER_ID = 1L;

    @ParameterizedTest
    @ValueSource(ints = {1})
    void 사용자_잔액_충전_성공(int amount) {
        // given
        User userA = new User(USER_ID, "userA", amount);
        given(userRepository.findById(USER_ID)).willReturn(userA);
        int userABalance = userA.addBalance(amount);
        given(userRepository.updateBalance(USER_ID, userABalance)).willReturn(new User(USER_ID, "userA", userABalance));

        // when
        UserResponse.User user = userService.chargeBalance(USER_ID, amount);

        // then
        assertThat(user.id()).isEqualTo(userA.id());
        assertThat(user.balance()).isEqualTo(userABalance);
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void id로_사용자를_찾을_수_없다면_잔액_충전_실패한다() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> userService.chargeBalance(USER_ID, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않는 사용자입니다.");

        verify(userRepository, never()).updateBalance(anyLong(), anyInt());
    }

    @Test
    void id로_사용자_조회시_사용자를_찾을_수_없다면_조회_실패() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(null);

        // when
        assertThatThrownBy(() -> userService.getUser(USER_ID))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않는 사용자입니다.");
    }

}