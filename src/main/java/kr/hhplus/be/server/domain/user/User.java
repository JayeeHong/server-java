package kr.hhplus.be.server.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User of(Long id, String name) {
        return new User(id, name);
    }

    public static User create(String name) {
        return User.of(null, name);
    }

    /** 이력 기준 총 잔액 계산 */
    public int calculateBalance(List<Balance> histories) {
        return histories.stream()
            .mapToInt(Balance::amount)
            .sum();
    }
}
