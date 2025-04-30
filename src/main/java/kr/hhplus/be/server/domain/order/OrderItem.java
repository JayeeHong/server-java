package kr.hhplus.be.server.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productId;

    private String productName;

    private Long unitPrice;

    private int quantity;

    private OrderItem(Long id, Order order, Long productId, String productName, Long unitPrice, int quantity) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItem of(Long id, Order order, Long productId, String productName, Long unitPrice, int quantity) {
        return new OrderItem(id, order, productId, productName, unitPrice, quantity);
    }

    public static OrderItem create(Long productId, String productName, Long unitPrice, int quantity) {
        return OrderItem.of(null, null, productId, productName, unitPrice, quantity);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getPrice() {
        return unitPrice * quantity;
    }
}
