package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.user.UserRequest.Charge;
import kr.hhplus.be.server.interfaces.user.UserResponse.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public Balance chargeBalance(Long userId, Charge request) {
        return null;
    }

    @Override
    public Balance getBalance(Long userId) {
        return null;
    }

//    @Override
//    @PostMapping("/{userId}/charge")
//    public UserResponse.Balance chargeBalance(
//        @PathVariable Long userId,
//        @RequestBody UserRequest.Charge request) {
//
//        return userService.chargeBalance(userId, request.getAmount());
//    }
//
//    @Override
//    @GetMapping("/{userId}/balance")
//    public UserResponse.Balance getBalance(@PathVariable Long userId) {
//        return userService.getUserBalance(userId);
//    }
}
