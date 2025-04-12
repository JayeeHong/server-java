package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.balance.BalanceRepository;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
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
        Balance balanceHistory = user.charge(amount);
        balanceRepository.save(balanceHistory);

        // 잔액 계산
        int totalBalance = balanceCalculator.calculate(user);

        return new UserResponse.Balance(user.id(), user.name(), totalBalance);
    }

    public UserResponse.Balance getUserBalance(Long userId) {
        User user = userRepository.findOrThrow(userId);
        int totalBalance = balanceCalculator.calculate(user);
        return new UserResponse.Balance(user.id(), user.name(), totalBalance);
    }

}
