package kr.hhplus.be.server.infrastructure.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;

    @Override
    public OrderItem save(OrderItem item) {
        return jpaOrderItemRepository.save(item);
    }

    @Override
    public List<OrderItem> saveAll(List<OrderItem> items) {
        return jpaOrderItemRepository.saveAll(items);
    }

    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        return jpaOrderItemRepository.findAllByOrderId(orderId);
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        jpaOrderItemRepository.deleteAllByOrderId(orderId);
    }
}
