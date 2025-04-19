package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderItemRepository {

    OrderItem save(OrderItem item);

    List<OrderItem> saveAll(List<OrderItem> items);

    List<OrderItem> findAllByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);
}
