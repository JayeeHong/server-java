package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Component;

@Component
public interface UserRepository {

    User findById(Long userId);

    User save(User user);

}
