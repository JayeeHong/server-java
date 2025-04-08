package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.Balance;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findById(long userId) {
        return new User((userId), "userA", new Balance(100));
    }

    @Override
    public User updateBalance(long userId, int amount) {
        return new User((userId), "userA", new Balance(amount));
    }
}
