package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.User;

public interface UserRepository {

    /**
     * 사용자 ID로 사용자 조회
     */
    User findById(Long userId);

    /**
     * 존재하지 않으면 예외를 던지는 기본 메서드
     */
    default User findOrThrow(Long userId) {
        User user = findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return user;
    }
}
