//package kr.hhplus.be.server.application.user;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.List;
//import kr.hhplus.be.server.domain.balance.Balance;
//import kr.hhplus.be.server.domain.balance.BalanceRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//public class BalanceCalculatorBenchmarkTest {
//
//    @Autowired
//    BalanceRepository balanceRepository;
//
//    Long userId = 1L;
//
//    @BeforeEach
//    void setup() {
//        if (balanceRepository.findAllByUserId(userId).isEmpty()) {
//            for (int i = 0; i < 100_000; i++) {
//                Balance balance = Balance.charge(userId, 100);
//                balanceRepository.save(balance);
//            }
//        }
//    }
//
//    @Test
//    void compare_balance_calculation_strategies() {
//        long startA = System.currentTimeMillis();
//        List<Balance> history = balanceRepository.findAllByUserId(userId);
//        int totalA = history.stream().mapToInt(Balance::amount).sum();
//        long endA = System.currentTimeMillis();
//
//        System.out.println("A (stream 합산): " + (endA - startA) + "ms");
//
//        long startB = System.currentTimeMillis();
//        int totalB = balanceRepository.getTotalBalance(userId);
//        long endB = System.currentTimeMillis();
//
//        System.out.println("B (쿼리 sum): " + (endB - startB) + "ms");
//
//        assertEquals(totalA, totalB);
//    }
//
//}
