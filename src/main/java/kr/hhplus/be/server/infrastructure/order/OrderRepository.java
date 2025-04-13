package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {

    Order save(Order order);

}
