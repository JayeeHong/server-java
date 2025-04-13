
package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequest {

    @Schema(description = "사용자 잔액 충전 요청")
    public static class Charge {

        @Schema(description = "충전할 금액", example = "1000", required = true)
        private int amount;

        public Charge() {}

        public Charge(int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }
    }

}
