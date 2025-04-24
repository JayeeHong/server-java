package kr.hhplus.be.server.application.order;

import java.util.ArrayList;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductService productService;
    private final CouponService couponService;
    private final UserService userService;
    private final OrderService orderService;

    @Transactional
    public Result placeOrder(OrderRequest.Command command) {
        // 1. 상품 조회 및 재고 차감
        List<Product> products = productService.getAndDecreaseStock(command.getItems());

        // 2. 상품 ID -> 상품 매핑
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

        // 3. 주문 항목 생성 및 총 금액 계산
        List<OrderItem> orderItems = new ArrayList<>();
        int totalAmount = 0;

        for (OrderRequest.Item item : command.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("존재하지 않는 상품입니다. ID=" + item.getProductId());
            }

            OrderItem orderItem = OrderItem.of(null, product.getId(), item.getQuantity());
            orderItems.add(orderItem);
            totalAmount += orderItem.calculateTotalPrice(product.getPrice());
        }

        // 4. 쿠폰 적용
        // TODO 쿠폰 적용
        Coupon usedCoupon = null;
//        if (command.getCouponId() != null) {
//            usedCoupon = couponService.useCoupon(command.getUserId(), command.getCouponId());
//            totalAmount = Math.max(0, totalAmount - usedCoupon.getDiscountAmount());
//        }

        // 5. 잔액 차감
        userService.validateAndPay(command.getUserId(), totalAmount);

        // 6. 주문 생성 및 저장 (연관관계 연결)
        Order order = Order.of(null, command.getUserId(), totalAmount);
        orderItems.forEach(order::addItem); // 양방향 연관관계 설정

        Order savedOrder = orderService.createOrder(order); // cascade = ALL이면 orderItem도 저장됨

        return Result.from(savedOrder, orderItems, usedCoupon);
    }
}
