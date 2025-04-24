package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {

    EXPIRED("만료"),
    REGISTERED("등록"),
    PUBLISHABLE("발급가능"),
    ;

    private final String description;

    private static final List<CouponStatus> CANNOT_PUBLISHABLE_STATUSES = List.of(EXPIRED, REGISTERED);

    public boolean cannotPublishable() {
        return CANNOT_PUBLISHABLE_STATUSES.contains(this);
    }
}
