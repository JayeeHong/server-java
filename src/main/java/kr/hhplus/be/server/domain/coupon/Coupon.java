package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

@Getter
public class Coupon {

    private final Long id;
    private final CouponCode code;
    private final Discount discount;
    private final IssuePeriod issuePeriod;
    private IssueCount issueCount;
    private final CouponType type;
    private final Long productId;

    public Coupon(Long id, CouponCode code, Discount discount, IssuePeriod issuePeriod, IssueCount issueCount, CouponType type, Long productId) {
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.issuePeriod = issuePeriod;
        this.issueCount = issueCount;
        this.type = type;
        this.productId = productId;
    }

    public boolean isExpired() {
        return issuePeriod.isExpired();
    }

    public boolean isAvailableToIssue() {
        return !isExpired() && issueCount.canIssue();
    }

    public void issue() {
        if (!isAvailableToIssue()) {
            throw new IllegalStateException("쿠폰 발급 불가");
        }
        this.issueCount = this.issueCount.issueOne();
    }

    public int applyDiscount(int price) {
        return discount.apply(price);
    }
}

