package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.User;
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
    @PostMapping("/{userId}/charge")
    public UserResponse.User chargeBalance(@PathVariable long userId, long amount) {
        return userService.chargeBalance(userId, amount);
    }

    @Override
    @GetMapping("/{userId}/balance")
    public UserResponse.User getBalance(@PathVariable long userId) {
        return userService.getBalance(userId);
    }

}
