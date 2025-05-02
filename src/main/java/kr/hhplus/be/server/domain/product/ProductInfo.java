package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProductInfo {

    @Getter
    public static class OrderProducts {

        private final List<OrderProduct> orderProducts;

        private OrderProducts(List<OrderProduct> orderProducts) {
            this.orderProducts = orderProducts;
        }

        public static OrderProducts of(List<OrderProduct> orderProducts) {
            return new OrderProducts(orderProducts);
        }
    }

    @Getter
    public static class OrderProduct {

        private final Long productId;
        private final String productName;
        private final long productPrice;
        private final int quantity;

        private OrderProduct(Long productId, String productName, long productPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.quantity = quantity;
        }

        public static OrderProduct of(Long productId, String productName, long productPrice, int quantity) {
            return new OrderProduct(productId, productName, productPrice, quantity);
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
