package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.User;

public interface UserRepository {

    User findById(long userId);

    User updateBalance(long userId, int amount);

}
