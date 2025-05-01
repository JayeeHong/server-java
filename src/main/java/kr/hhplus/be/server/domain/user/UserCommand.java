package kr.hhplus.be.server.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCommand {

    @Getter
    public static class Create {
        private final String name;

        private Create(String name) {
            this.name = name;
        }

        public static Create of(String name) {
            return new Create(name);
        }
    }
}
