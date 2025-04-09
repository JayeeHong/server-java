package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    @Schema(description = "사용자 정보")
    public record User(
        @Schema(description = "사용자 ID", example = "1")
        long id,
        @Schema(description = "사용자명", example = "사용자")
        String name,
        @Schema(description = "잔액", example = "100000")
        int balance
    ) {

    }

}
