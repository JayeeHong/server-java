package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceCalculator balanceCalculator;

    @Transactional
    public UserResponse.Balance chargeBalance(Long userId, int amount) {
        User user = userRepository.findOrThrow(userId);

        // 충전 이력 생성 및 저장
        Balance balanceHistory = Balance.create(userId, amount);
        balanceRepository.save(balanceHistory);

        // 잔액 계산
        int totalBalance = balanceCalculator.calculate(userId);

        return new UserResponse.Balance(user.getId(), user.getName(), totalBalance);
    }

    public UserResponse.Balance getUserBalance(Long userId) {
        User user = userRepository.findOrThrow(userId);
        int totalBalance = balanceCalculator.calculate(userId);

        return new UserResponse.Balance(user.getId(), user.getName(), totalBalance);
    }

    @Transactional
    public void validateAndPay(Long userId, int totalAmount) {
        User user = userRepository.findOrThrow(userId);

        int currentBalance = balanceCalculator.calculate(userId);
        if (currentBalance < totalAmount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        balanceRepository.save(Balance.create(userId, totalAmount));
    }

}
