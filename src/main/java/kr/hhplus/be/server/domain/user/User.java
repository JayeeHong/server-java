package kr.hhplus.be.server.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record User(
    long id,
    String name,
    int balance
) {

}
