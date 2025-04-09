package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public class IssuePeriod {
    private final LocalDateTime endDate;

    public IssuePeriod(LocalDateTime endDate) {
        if (endDate == null) throw new IllegalArgumentException("종료일은 필수입니다.");
        this.endDate = endDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}

