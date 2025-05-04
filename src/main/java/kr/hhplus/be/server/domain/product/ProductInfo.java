package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInfo {

    @Getter
    public static class OrderItems {

        private final List<OrderItem> orderItems;

        private OrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        public static OrderItems of(List<OrderItem> orderItems) {
            return new OrderItems(orderItems);
        }
    }

    @Getter
    public static class OrderItem {

        private final Long productId;
        private final String productName;
        private final long productPrice;
        private final int quantity;

        private OrderItem(Long productId, String productName, long productPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.quantity = quantity;
        }

        public static OrderItem of(Long productId, String productName, long productPrice, int quantity) {
            return new OrderItem(productId, productName, productPrice, quantity);
        }
    }

    @Getter
    public static class Products {

        private final List<Product> products;

        private Products(List<Product> products) {
            this.products = products;
        }

        public static Products of(List<Product> products) {
            return new Products(products);
        }
    }

    @Getter
    public static class Product {

        private final Long productId;
        private final String productName;
        private final long productPrice;

        private Product(Long productId, String productName, long productPrice) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
        }

        public static Product of(Long productId, String productName, long productPrice) {
            return new Product(productId, productName, productPrice);
        }
    }

}
