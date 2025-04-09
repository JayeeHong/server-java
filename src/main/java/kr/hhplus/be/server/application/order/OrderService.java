package kr.hhplus.be.server.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public int calculateTotalAmount(List<Product> products) {
        return products.stream()
            .mapToInt(p -> p.getPrice().value())
            .sum();
    }

    public int applyCouponDiscount(int totalAmount, Coupon coupon) {
        if (coupon == null) return totalAmount;
        if (coupon.getType() == CouponType.PERCENTAGE) {
            return totalAmount - (totalAmount * coupon.getDiscount().getAmount() / 100);
        } else {
            return totalAmount - coupon.getDiscount().getAmount();
        }
    }

    public void validateUserBalance(User user, int finalAmount) {
        if (user.getBalance().amount() < finalAmount) {
            throw new IllegalStateException("사용자의 잔액이 부족합니다.");
        }
    }

    public void deductUserBalance(User user, int amount) {
        user.deductBalance(amount);
    }

    public Order createOrder(User user, List<Product> products, Coupon coupon, int finalAmount) {
        List<OrderItem> orderItems = products.stream()
            .map(p -> new OrderItem(null, null, p.getId(), 1, p.getPrice().value()))
            .collect(Collectors.toList());

        return new Order(null, user.getId(), finalAmount, OrderStatus.PAY_COMPLETE, orderItems, System.currentTimeMillis(), System.currentTimeMillis());
    }
}