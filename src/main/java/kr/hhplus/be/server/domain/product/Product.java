package kr.hhplus.be.server.domain.product;

public class Product {

    private final Long id;
    private final String name;
    private final int price;
    private final int stock;

    private Product(Long id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product of(Long id, String name, int price, int stock) {
        return new Product(id, name, price, stock);
    }

    /**
     * 재고를 감소시키는 메서드 (주문 시 사용)
     * @throws IllegalStateException 재고 부족 시 예외
     */
    public Product decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        return new Product(this.id, this.name, this.price, this.stock - quantity);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    public int stock() {
        return stock;
    }
}
