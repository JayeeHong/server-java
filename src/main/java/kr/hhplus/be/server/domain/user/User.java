package kr.hhplus.be.server.domain.user;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;

public class User {

    private final Long id;
    private final String name;

    private User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User of(Long id, String name) {
        return new User(id, name);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    /**
     * 이력 기준 총 잔액 계산
     */
    public int calculateBalance(List<Balance> histories) {
        return histories.stream()
            .mapToInt(Balance::amount)
            .sum();
    }

}
