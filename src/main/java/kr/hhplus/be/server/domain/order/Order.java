package kr.hhplus.be.server.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int totalAmount;

    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderedAt;

    private Order(Long id, Long userId, int totalAmount) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = OrderStatus.WAIT;
        this.orderedAt = LocalDateTime.now();
    }

    public static Order of(Long id, Long userId, int totalAmount) {
        return new Order(id, userId, totalAmount);
    }

    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }
}
