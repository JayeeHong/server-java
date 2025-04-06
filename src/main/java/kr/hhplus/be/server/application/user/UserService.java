package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserResponse.User chargeBalance(long userId, long amount) {

        // 사용자 식별자 유효성 체크

        // 사용자 식별자로 사용자의 잔액 조회

        // amount 값 유효성 체크
        // 1) 0 초과인지
        // 2) 충전 가능한 최대 금액인지
        // 3) 충전 가능한 최소 금액인지

        // 잔액 충전

        return new UserResponse.User(1L, "홍길동", 1000);
    }

    public UserResponse.User getBalance(long userId) {

        // 사용자 식별자 유효성 체크

        // 사용자 식별자로 사용자의 잔액 조회

        return new UserResponse.User(1L, "홍길동", 1000);
    }

}
