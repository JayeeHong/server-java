
package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductService productService;
    private final CouponService couponService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderResponse.Summary placeOrder(OrderRequest.Command command) {
        // 1. 상품 재고 차감
        List<OrderItem> items = productService.getAndDecreaseStock(command.getItems()).stream()
            .map(product -> {
                int quantity = command.getItems().stream()
                    .filter(i -> i.getProductId().equals(product.id()))
                    .findFirst()
                    .map(OrderRequest.Item::getQuantity)
                    .orElseThrow();
                return OrderItem.of(null, null, product.id(), quantity);
            })
            .toList();

        // 2. 총 금액 계산
        Map<Long, Integer> quantityMap = command.getItems().stream()
            .collect(Collectors.toMap(OrderRequest.Item::getProductId, OrderRequest.Item::getQuantity));

        int totalAmount = items.stream()
            .mapToInt(item -> {
                int price = productService.getPrice(item.productId());
                return price * quantityMap.get(item.productId());
            })
            .sum();

        // 3. 쿠폰 적용
        Coupon usedCoupon = null;
        if (command.getCouponId() != null) {
            usedCoupon = couponService.validateAndUseCoupon(command.getUserId(), command.getCouponId());
            totalAmount -= usedCoupon.discountAmount();
        }

        // 4. 사용자 잔액 차감
        userService.validateAndPay(command.getUserId(), totalAmount);

        // 5. 주문 생성 및 저장
        Order order = Order.of(null, command.getUserId(), totalAmount, items);
        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            orderItemRepository.save(OrderItem.of(null, savedOrder.id(), item.productId(), item.quantity()));
        }

        // 6. 응답 반환
        return OrderResponse.Summary.from(savedOrder, usedCoupon);
    }
}
