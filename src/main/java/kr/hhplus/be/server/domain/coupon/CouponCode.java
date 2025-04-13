package kr.hhplus.be.server.domain.coupon;

public class CouponCode {
    private final String value;

    public CouponCode(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("쿠폰 코드가 유효하지 않습니다.");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

