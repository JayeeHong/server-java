package kr.hhplus.be.server.domain.balance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    @DisplayName("잔고를 저장한다")
    void save() {

        // given
        Balance balance = Balance.of(1L, 1_000L);

        // when
        balanceRepository.save(balance);

        // then
        assertThat(balance.getId()).isNotNull();
    }

    @Test
    @DisplayName("잔고가 없는 사용자의 잔고를 조회한다")
    void findBalanceByUserIdNoBalance() {

        // when
        Optional<Balance> result = balanceRepository.findOptionalByUserId(1L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("잔고가 있는 사용자의 잔고를 조회한다")
    void findBalanceByUserId() {

        // given
        Balance balance = Balance.of(1L, 1_000L);
        balanceRepository.save(balance);

        // when
        Optional<Balance> result = balanceRepository.findOptionalByUserId(1L);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUserId()).isEqualTo(1L);
        assertThat(result.get().getAmount()).isEqualTo(1_000L);
    }
}
