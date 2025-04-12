
package kr.hhplus.be.server.domain.coupon;

public class Coupon {

    private final Long id;
    private final String name;
    private final int discountAmount;
    private final int stock;

    private Coupon(Long id, String name, int discountAmount, int stock) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.stock = stock;
    }

    public static Coupon of(Long id, String name, int discountAmount, int stock) {
        return new Coupon(id, name, discountAmount, stock);
    }

    public Coupon issue() {
        if (stock <= 0) {
            throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        }
        return new Coupon(id, name, discountAmount, stock - 1);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int discountAmount() {
        return discountAmount;
    }

    public int stock() {
        return stock;
    }
}
