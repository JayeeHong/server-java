package kr.hhplus.be.server.domain.coupon;

public class Discount {
    private final int amount;

    public Discount(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("할인 금액은 0보다 커야 합니다.");
        this.amount = amount;
    }

    public int apply(int price) {
        return Math.max(0, price - amount);
    }

    public int getAmount() {
        return amount;
    }
}

