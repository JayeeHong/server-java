package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CouponResponse {

    @Getter
    @RequiredArgsConstructor
    @Schema(description = "사용자가 보유한 쿠폰 정보")
    public static class UserCoupon {

        @Schema(description = "쿠폰 ID", example = "1")
        private final Long couponId;

        @Schema(description = "쿠폰 이름", example = "10% 할인쿠폰")
        private final String couponName;

        @Schema(description = "할인 금액", example = "1000")
        private final int discountAmount;

        @Schema(description = "발급 일시", example = "2024-04-10T10:15:30")
        private final LocalDateTime issuedAt;

        public static UserCoupon from(kr.hhplus.be.server.domain.user.UserCoupon entity) {
            return new UserCoupon(
                entity.coupon().id(),
                entity.coupon().name(),
                entity.coupon().discountAmount(),
                entity.issuedAt()
            );
        }
    }

    public static List<UserCoupon> translate(List<kr.hhplus.be.server.domain.user.UserCoupon> entity) {
        return entity.stream()
            .map(UserCoupon::from)
            .toList();
    }
}
