package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {
    
    @Schema(description = "사용자 잔액 정보")
    public static class Balance {
        @Schema(description = "사용자 ID", example = "1")
        private final Long userId;
        @Schema(description = "사용자명", example = "사용자")
        private final String name;
        @Schema(description = "잔액", example = "100000")
        private final int balance;
    
        public Balance(Long userId, String name, int balance) {
            this.userId = userId;
            this.name = name;
            this.balance = balance;
        }
    
        public Long getUserId() {
            return userId;
        }
    
        public String getName() {
            return name;
        }
    
        public int getBalance() {
            return balance;
        }
    }


}
