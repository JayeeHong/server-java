package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "잔고 API", description = "사용자 잔고 관련 기능")
public interface BalanceApi {

    @Operation(summary = "사용자 잔고 조회")
    BalanceResponse.Balance getBalance(@PathVariable("id") Long id);

    @Operation(summary = "사용자 잔고 충전")
    void chargeBalance(@PathVariable("id") Long id, @RequestBody BalanceRequest.Charge request);
}
