package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.common.Amount;
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

        // amount 값 유효성 체크
        Amount chargeAmount = Amount.of(amount);
        // 1) 0 초과인지
        // 2) 충전 가능한 최대 금액인지
        // 3) 충전 가능한 최소 금액인지

        // 4) 충전 후 금액 체크
        int addedBalance = findUser.addBalance(chargeAmount.getValue());

        // 잔액 충전
        User user = userRepository.updateBalance(userId, addedBalance);

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
