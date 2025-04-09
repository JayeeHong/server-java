package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.interfaces.user.UserResponse;
import lombok.Getter;

@Getter
public class User {

    private final long id;
    private final String name;
    private Balance balance;

    public User(Long id) {
        this.id = id;
        this.name = "";
        this.balance = new Balance(0);
    }

    public User(Long id, String name, Balance balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public void charge(int amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(int amount) {
        this.balance = this.balance.subtract(amount);
    }

    public boolean canAfford(int amount) {
        return this.balance.isEnough(amount);
    }

    public int getBalanceAmount() {
        return this.balance.amount();
    }

    public void deductBalance(int amount) {
        if (!canAfford(amount)) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        withdraw(amount);
    }

    public UserResponse.User translateUser(User user) {
        return new UserResponse.User(user.id, user.name, user.balance.amount());
    }

}
