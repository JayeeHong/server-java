package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Order save(Order order) {
        return null;
    }

    // TODO OrderJpaRepository 와 같은 CRUD 해올 객체 추가

}
