package kr.hhplus.be.server.application.order;

import java.util.List;
import java.util.stream.Collectors;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.infrastructure.coupon.CouponRepository;
import kr.hhplus.be.server.infrastructure.order.OrderRepository;
import kr.hhplus.be.server.infrastructure.product.ProductRepository;
import kr.hhplus.be.server.infrastructure.user.UserCouponRepository;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest.CreateOrder;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public OrderResponse.Order placeOrder(long userId, CreateOrder request) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 2. 상품 조회
        List<Product> products = request.orderItems().stream()
            .map(item -> productRepository.findById(item.productId()))
            .collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        // 3. 쿠폰 조회 (선택적)
        Coupon coupon = null;
        if (request.userCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findById(request.userCouponId());
            if (userCoupon == null || userCoupon.isUsed()) {
                throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
            }
            coupon = userCoupon.getCoupon();
        }

        // 4. 최종 결제 금액 계산
        int totalAmount = orderService.calculateTotalAmount(products);
        int finalAmount = orderService.applyCouponDiscount(totalAmount, coupon);

        // 5. 사용자 잔액 확인 및 차감
        orderService.validateUserBalance(user, finalAmount);
        orderService.deductUserBalance(user, finalAmount);

        // 6. 주문 생성 및 저장
        Order order = orderService.createOrder(user, products, coupon, finalAmount);
        orderRepository.save(order);

        // 7. 쿠폰 사용 처리
        if (request.userCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findById(request.userCouponId());
            userCoupon.use();
            userCouponRepository.save(userCoupon);
        }

        // 8. 사용자 정보 저장
        userRepository.save(user);

        return order.toResponse();
    }
}