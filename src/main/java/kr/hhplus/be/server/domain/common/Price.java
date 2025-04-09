package kr.hhplus.be.server.domain.common;

public class Price {
    private final int value;

    public Price(int value) {
        if (value < 0) throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        this.value = value;
    }

    public int value() {
        return value;
    }

    public Price add(Price other) {
        return new Price(this.value + other.value);
    }

    public Price subtract(Price other) {
        if (this.value < other.value)
            throw new IllegalArgumentException("가격 차감 실패: 부족한 값");
        return new Price(this.value - other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}