package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.application.order.OrderInfoEventPublisher;
import kr.hhplus.be.server.domain.order.OrderCommand.PaidItems;
import kr.hhplus.be.server.domain.order.OrderInfo.PaidItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderExternalClient orderExternalClient;
    private final OrderInfoEventPublisher orderInfoEventPublisher;

    @Transactional
    public OrderInfo.Order createOrder(OrderCommand.Create command) {

        List<OrderItem> orderItems = new ArrayList<>();
        command.getItems().forEach(item -> {
            OrderItem orderItem = OrderItem.create(item.getProductId(), item.getProductName(),
                item.getUnitPrice(), item.getQuantity());
            orderItems.add(orderItem);
        });

        Order order = Order.create(command.getUserId(), command.getUserCouponId(),
            command.getDiscountPrice(), orderItems);
        orderRepository.save(order);

        return OrderInfo.Order.of(order.getId(), order.getTotalPrice(), order.getDiscountPrice(),
            order.getOrderStatus());
    }

    @Transactional
    public void paidOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.paid(LocalDateTime.now());

//        orderExternalClient.sendOrderMessage(order);
        orderInfoEventPublisher.success(OrderInfoEvent.toOrderInfoEvent(order));
    }

    public OrderInfo.PaidItems getPaidItems(OrderCommand.DateQuery command) {
        PaidItems queryCommand = command.toPaidProductsQuery(OrderStatus.PAY_COMPLETE);
        List<PaidItem> paidItems = orderRepository.findPaidItems(queryCommand);

        return OrderInfo.PaidItems.of(paidItems);
    }

    public OrderInfo.Order getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);

        return OrderInfo.Order.of(order.getId(), order.getTotalPrice(), order.getDiscountPrice(),
            order.getOrderStatus());
    }
}