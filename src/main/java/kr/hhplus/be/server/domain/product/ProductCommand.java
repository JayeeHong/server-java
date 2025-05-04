package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCommand {

    @Getter
    public static class OrderProducts {

        private final List<OrderItem> orderItems;

        private OrderProducts(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        public static OrderProducts of(List<OrderItem> orderItems) {
            return new OrderProducts(orderItems);
        }
    }

    @Getter
    public static class OrderItem {

        private final Long productId;
        private final int quantity;

        private OrderItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public static OrderItem of(Long productId, int quantity) {
            return new OrderItem(productId, quantity);
        }
    }

    @Getter
    public static class Products {

        private final List<Long> productIds;

        private Products(List<Long> productIds) {
            this.productIds = productIds;
        }

        public static Products of(List<Long> productIds) {
            return new Products(productIds);
        }
    }

    @Getter
    public static class Product {

        private final Long productId;

        private Product(Long productId) {
            this.productId = productId;
        }

        public static Product of(Long productId) {
            return new Product(productId);
        }

        public static ProductInfo.OrderItem toOrderProductInfo(kr.hhplus.be.server.domain.product.Product product) {
            return ProductInfo.OrderItem.of(
                product.getId(), product.getName(), product.getPrice(), product.getQuantity()
            );
        }

        public static ProductInfo.Product toProductInfo(kr.hhplus.be.server.domain.product.Product product) {
            return ProductInfo.Product.of(product.getId(), product.getName(), product.getPrice());
        }
    }

    @Getter
    public static class Create {

        private String name;
        private long price;
        private int quantity;
        private ProductStatus status;

        private Create(String name, long price, int quantity, ProductStatus status) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.status = status;
        }

        public static Create of(String name, long price, int quantity, ProductStatus status) {
            return new Create(name, price, quantity, status);
        }
    }

}
