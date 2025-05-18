package kr.hhplus.be.server.domain.product;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductOrderedEvent {

    private final List<ProductInfo.OrderItem> items;
    private final LocalDate orderDate;

    public ProductOrderedEvent(List<ProductInfo.OrderItem> items, LocalDate orderDate) {
        this.items = items;
        this.orderDate = orderDate;
    }
}
