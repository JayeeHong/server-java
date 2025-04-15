package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    Order findById(Long id);

    List<Order> findAllByUserId(Long userId);

    void deleteById(Long id);
}
