package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.interfaces.user.UserResponse;

public record User(
    long id,
    String name,
    int balance
) {

    private static final long MIN_BALANCE = 0;
    private static final long MAX_BALANCE = 1000000; //100만원

    public User {
        if (balance < MIN_BALANCE) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        if (balance > MAX_BALANCE) {
            throw new IllegalArgumentException("최대 잔액은 1,000,000원입니다.");
        }
    }

    public static User empty(long id, String name) {
        return new User(id, name, 0);
    }

    public int addBalance(int amount) {
        int addedBalance = balance + amount;
        validateBalance(addedBalance);

        return addedBalance;
    }

    private void validateBalance(int balance) {
        if (balance > MAX_BALANCE) {
            throw new IllegalArgumentException("최대 잔액은 1,000,000원입니다.");
        }
    }

    public UserResponse.User translateUser(User user) {
        return new UserResponse.User(user.id(), user.name(), user.balance());
    }

}
