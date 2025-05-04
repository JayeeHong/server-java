package kr.hhplus.be.server.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long userCouponId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private long totalPrice;

    private long discountPrice;

    private LocalDateTime paidAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(Long id, Long userId, Long userCouponId, long discountPrice, List<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderStatus = OrderStatus.CREATED;

        orderItems.forEach(this::addOrderItem);

        this.totalPrice = calculateTotalPrice(orderItems) - discountPrice;
        this.discountPrice = discountPrice;
    }

    public static Order of(Long id, Long userId, Long userCouponId, long discountPrice, List<OrderItem> orderItems) {
        return new Order(id, userId, userCouponId, discountPrice, orderItems);
    }

    public static Order create(Long userId, Long userCouponId, long discountPrice, List<OrderItem> orderItems) {
        validateOrderItems(orderItems);

        return Order.of(null, userId, userCouponId, discountPrice, orderItems);
    }

    public void paid(LocalDateTime paidAt) {
        this.orderStatus = OrderStatus.PAY_COMPLETE;
        this.paidAt = paidAt;
    }

    private long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(OrderItem::getPrice)
            .sum();
    }

    private static void validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }
    }

    private void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
