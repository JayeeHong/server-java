package kr.hhplus.be.server.coupon;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;

    @Override
    @PostMapping("/issue/{userId}/{couponId}")
    public UserCoupon issueCoupon(@PathVariable long userId, @PathVariable long couponId) {
        return couponService.issueCoupon(userId, couponId);
    }

    @Override
    @GetMapping("/coupons/{userId}")
    public List<UserCoupon> getCoupons(@PathVariable long userId) {
        return couponService.getCoupons(userId);
    }

}
