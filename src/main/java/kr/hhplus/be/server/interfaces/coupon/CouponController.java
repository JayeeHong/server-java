package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.application.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController implements CouponApi {

    private final CouponService couponService;

    @Override
    @PostMapping("/{couponId}/issue/{userId}")
    public CouponResponse.UserCoupon issueCoupon(@PathVariable Long userId, @PathVariable Long couponId) {
        return couponService.issueCoupon(userId, couponId);
    }

    @Override
    @GetMapping("/{userId}/list")
    public List<CouponResponse.UserCoupon> getUserCoupons(@PathVariable Long userId) {
        return couponService.getUserCoupons(userId);
    }
}
