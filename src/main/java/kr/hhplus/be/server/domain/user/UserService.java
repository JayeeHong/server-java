package kr.hhplus.be.server.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfo.User getUser(Long userId) {
        User user = userRepository.findById(userId);
        return UserInfo.User.of(user.getId(), user.getName());
    }

    @Transactional
    public UserInfo.User createUser(UserCommand.Create command) {
        User user = User.create(command.getName());
        userRepository.save(user);

        return user.toUserInfo(user);
    }

}
