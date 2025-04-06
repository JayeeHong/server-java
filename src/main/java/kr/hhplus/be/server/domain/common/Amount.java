package kr.hhplus.be.server.domain.common;

public class Amount {

    private final int value;

    private static final long MIN_BALANCE = 0;
    private static final long MAX_BALANCE = 1000000; //100만원

    private Amount(int value) {
        validate(value);
        this.value = value;
    }

    public static Amount of(int amount) {
        return new Amount(amount);
    }

    private void validate(int amount) {
        if (amount <= MIN_BALANCE) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }

        if (amount > MAX_BALANCE) {
            throw new IllegalArgumentException("충전 가능한 최대 금액은 1,000,000원입니다.");
        }
    }

    public int getValue() {
        return value;
    }

}
