package kr.hhplus.be.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping("/charge/balance/{userId}")
    public User chargeBalance(@PathVariable long userId, long amount) {
        return userService.chargeBalance(userId, amount);
    }

    @Override
    @GetMapping("/balance/{userId}")
    public User getBalance(@PathVariable long userId) {
        return userService.getBalance(userId);
    }

}
