package kr.hhplus.be.server.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장 전에는 사용자를 조회할 수 없다")
    void findByIdFailTest() {

        // given
        Long userId = 1L;

        // when, then
        assertThatThrownBy(() -> userRepository.findById(userId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사용자 저장 후 사용자를 조회할 수 있다")
    void findByIdSuccessTest() {

        // given
        User user = User.create("userA");
        userRepository.save(user);

        // when
        User findUser = userRepository.findById(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }
}
