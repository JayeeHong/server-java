package kr.hhplus.be.server.application.product;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResult {

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
        private final Long productPrice;
        private final int quantity;

        private Product(Long productId, String productName, Long productPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.quantity = quantity;
        }

        public static Product of(Long productId, String productName, Long productPrice, int quantity) {
            return new Product(productId, productName, productPrice, quantity);
        }
    }
}
