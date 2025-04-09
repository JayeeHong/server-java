package kr.hhplus.be.server.domain.coupon;

public class IssueCount {
    private final int value;

    public IssueCount(int value) {
        if (value < 0) throw new IllegalArgumentException("발급 횟수는 0 이상이어야 합니다.");
        this.value = value;
    }

    public boolean canIssue() {
        return value > 0;
    }

    public IssueCount issueOne() {
        if (!canIssue()) throw new IllegalStateException("발급 횟수 초과");
        return new IssueCount(value - 1);
    }

    public int getValue() {
        return value;
    }
}

