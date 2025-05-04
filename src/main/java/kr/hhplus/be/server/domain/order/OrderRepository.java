package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    Order findById(Long id);

    List<OrderItem> findOrderIdsIn(List<Long> orderIds);

    List<OrderInfo.PaidItem> findPaidItems(OrderCommand.PaidItems command);

    void deleteById(Long id);
}
