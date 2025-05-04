package kr.hhplus.be.server.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {

    @Getter
    public static class User {
        private final Long userId;
        private final String name;

        private User(Long userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        public static User of(Long userId, String name) {
            return new User(userId, name);
        }
    }
}
