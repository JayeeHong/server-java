package kr.hhplus.be.server.infrastructure.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Order findById(Long orderId) {
        return jpaOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }

    @Override
    public List<Order> findAllByUserId(Long userId) {
        return jpaOrderRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long orderId) {
        jpaOrderRepository.deleteById(orderId);
    }
}
