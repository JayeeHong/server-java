package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.domain.user.UserCommand.Create;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유효하지 않은 ID로 사용자를 조회하면 조회에 실패한다")
    void getUserWithInvalidId() {

        // given
        when(userRepository.findById(anyLong()))
            .thenThrow(new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // when, then
        assertThatThrownBy(() -> userService.getUser(anyLong()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효한 ID로 사용자를 조회하면 조회에 성공한다")
    void getUserWithValidId() {

        // given
        when(userRepository.findById(anyLong()))
            .thenReturn(User.create("userA"));

        // when
        UserInfo.User user = userService.getUser(1L);

        // then
        assertThat(user.getName()).isEqualTo("userA");
    }

    @Test
    @DisplayName("사용자를 저장한다")
    void saveUser() {

        // given
        Create user = Create.of("userA");

        // when
        userService.createUser(user);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
