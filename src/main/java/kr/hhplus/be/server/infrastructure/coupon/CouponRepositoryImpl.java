package kr.hhplus.be.server.infrastructure.coupon;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCode;
import kr.hhplus.be.server.domain.coupon.CouponHistory;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.Discount;
import kr.hhplus.be.server.domain.coupon.IssueCount;
import kr.hhplus.be.server.domain.coupon.IssuePeriod;
import kr.hhplus.be.server.domain.user.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    @Override
    public Coupon findById(long couponId) {
        return new Coupon(1L, new CouponCode("COU20250403"), new Discount(20),
            new IssuePeriod(LocalDateTime.of(2025, 5, 1, 0, 0)),
            new IssueCount(1),
            CouponType.PERCENTAGE,
            1L);
    }

    @Override
    public List<UserCoupon> findAllByUserId(long userId) {

        Coupon couponEx = new Coupon(1L, new CouponCode("COU20250403"), new Discount(20),
            new IssuePeriod(LocalDateTime.of(2025, 5, 1, 0, 0)),
            new IssueCount(1),
            CouponType.PERCENTAGE,
            1L);

        return List.of(
            new UserCoupon(1L, 1L, couponEx, LocalDateTime.now()),
            new UserCoupon(2L, 1L, couponEx, LocalDateTime.now())
        );
    }

    @Override
    public CouponHistory save(UserCoupon userCoupon) {
        return null;
    }

    @Override
    public UserCoupon findByUserIdAndCouponId(long userId, long couponId) {
        return null;
    }
}
