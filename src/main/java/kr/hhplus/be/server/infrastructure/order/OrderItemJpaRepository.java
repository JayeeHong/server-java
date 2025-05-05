package kr.hhplus.be.server.infrastructure.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderIdIn(List<Long> orderIds);

}
