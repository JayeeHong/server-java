package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.Balance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse.User chargeBalance(long userId, int amount) {

        // 사용자 식별자 유효성 체크
        User findUser = userRepository.findById(userId);
        if (findUser == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        // 잔고 객체 생성
        Balance balance = new Balance(amount);

        // 4) 충전 후 금액 체크
        findUser.charge(balance.amount());

        // 잔액 충전
        User user = userRepository.updateBalance(userId, findUser.getBalanceAmount());

        return user.translateUser(user);
    }

    public UserResponse.User getUser(long userId) {

        // 사용자 식별자 유효성 체크
        User findUser = userRepository.findById(userId);
        if (findUser == null) {
            throw new IllegalArgumentException("유효하지 않는 사용자입니다.");
        }

        return findUser.translateUser(findUser);
    }

}
