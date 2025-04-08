package kr.hhplus.be.server.domain.common;

public class Stock {
    private final int quantity;

    public Stock(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
        this.quantity = quantity;
    }

    public Stock decrease(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("재고 부족");
        }

        return new Stock(this.quantity - amount);
    }

    public Stock increase(int amount) {
        return new Stock(this.quantity + amount);
    }

    public boolean isSoldOut() {
        return quantity == 0;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(quantity);
    }
}
