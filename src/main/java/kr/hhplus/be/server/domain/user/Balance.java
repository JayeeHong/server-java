package kr.hhplus.be.server.domain.user;

public class Balance {
    private final int amount;

    public Balance(int amount) {
        if (amount < 0) throw new IllegalArgumentException("잔고는 음수일 수 없습니다.");
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

    public Balance add(int value) {
        if (value < 0) throw new IllegalArgumentException("음수는 추가할 수 없습니다.");
        return new Balance(this.amount + value);
    }

    public Balance subtract(int value) {
        if (value < 0) throw new IllegalArgumentException("음수는 차감할 수 없습니다.");
        if (this.amount < value) throw new IllegalArgumentException("잔고 부족");
        return new Balance(this.amount - value);
    }

    public boolean isEnough(int value) {
        return this.amount >= value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance)) return false;
        Balance balance = (Balance) o;
        return amount == balance.amount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(amount);
    }

    @Override
    public String toString() {
        return amount + "원";
    }
}
