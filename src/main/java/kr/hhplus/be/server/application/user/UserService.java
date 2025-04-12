package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceCalculator balanceCalculator;

    public UserResponse.Balance chargeBalance(Long userId, int amount) {
        User user = userRepository.findOrThrow(userId);

        // 충전 이력 생성 및 저장
        Balance balanceHistory = Balance.charge(userId, amount);
        balanceRepository.save(balanceHistory);

        // 잔액 계산
        List<Balance> history = balanceRepository.findAllByUserId(userId);
        int totalBalance = BalanceCalculator.calculate(history);

        return new UserResponse.Balance(user.id(), user.name(), totalBalance);
    }

    public UserResponse.Balance getUserBalance(Long userId) {
        User user = userRepository.findOrThrow(userId);
        List<Balance> history = balanceRepository.findAllByUserId(userId);
        int totalBalance = BalanceCalculator.calculate(history);

        return new UserResponse.Balance(user.id(), user.name(), totalBalance);
    }

    public void validateAndPay(Long userId, int totalAmount) {
        User user = userRepository.findOrThrow(userId);

        List<Balance> history = balanceRepository.findAllByUserId(userId);
        int currentBalance = BalanceCalculator.calculate(history);
        if (currentBalance < totalAmount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        balanceRepository.save(Balance.deduct(userId, totalAmount));
    }

}
