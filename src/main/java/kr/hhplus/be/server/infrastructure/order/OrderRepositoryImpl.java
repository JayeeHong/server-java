package kr.hhplus.be.server.infrastructure.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand.PaidItems;
import kr.hhplus.be.server.domain.order.OrderInfo.PaidItem;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order findById(Long orderId) {
        return orderJpaRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }

    @Override
    public List<OrderItem> findOrderIdsIn(List<Long> orderIds) {
        return List.of();
    }

    @Override
    public List<PaidItem> findPaidItems(PaidItems command) {
        return List.of();
    }

    @Override
    public void deleteById(Long orderId) {
        orderJpaRepository.deleteById(orderId);
    }
}
