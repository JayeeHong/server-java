package kr.hhplus.be.server.interfaces.balance;

import jakarta.validation.Valid;
import kr.hhplus.be.server.application.balance.BalanceFacade;
import kr.hhplus.be.server.application.balance.BalanceResult;
import kr.hhplus.be.server.interfaces.balance.BalanceResponse.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class BalanceController implements BalanceApi {

    private final BalanceFacade balanceFacade;

    @Override
    @GetMapping("/{id}/balance")
    public Balance getBalance(@PathVariable("id") Long id) {
        BalanceResult.Balance balance = balanceFacade.getBalance(id);
        return BalanceResponse.Balance.of(balance);
    }

    @Override
    @PostMapping("{id}/balance/charge")
    public void chargeBalance(@PathVariable("id") Long id, @Valid @RequestBody BalanceRequest.Charge request) {
        balanceFacade.chargeBalance(request.toCriteria(id));
    }
}
