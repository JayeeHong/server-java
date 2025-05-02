package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long price;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Product(Long id, String name, long price, int quantity, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public static Product of(Long id, String name, long price, int quantity, ProductStatus status) {
        return new Product(id, name, price, quantity, status);
    }

    public static Product create(String name, long price, int quantity, ProductStatus status) {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
        validateProductStatus(status);

        return Product.of(null, name, price, quantity, status);
    }

    public boolean cannotSelling() {
        return status.cannotSelling();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
    }

    private static void validatePrice(long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("상품 금액은 0보다 커야 합니다.");
        }
    }

    private static void validateProductStatus(ProductStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("상품 판매 상태는 필수입니다.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("상품 수량은 0 이상이어야 합니다.");
        }
    }

    public void decreaseStock(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }

        this.quantity -= quantity;
    }
}
