package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kr.hhplus.be.server.domain.user.UserCommand.Create;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("유효하지 않은 사용자는 조회할 수 없다")
    void cannotGetInvalidUser() {

        // when, then
        assertThatThrownBy(() -> userService.getUser(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자를 조회한다")
    void getUser() {

        // given
        Create command = Create.of("userA");

        // when
        UserInfo.User savedUser = userService.createUser(command);

        // then
        assertThat(savedUser.getUserId()).isNotNull();
    }
}
