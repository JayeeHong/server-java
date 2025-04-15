package kr.hhplus.be.server.infrastructure.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

}
