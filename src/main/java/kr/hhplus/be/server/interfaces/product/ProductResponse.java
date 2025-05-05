package kr.hhplus.be.server.interfaces.product;

import java.util.List;
import kr.hhplus.be.server.application.product.ProductResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponse {

    @Getter
    @NoArgsConstructor
    public static class Products {

        private List<Product> products;

        private Products(List<Product> products) {
            this.products = products;
        }

        public static Products of(ProductResult.Products products) {
            return new Products(products.getProducts().stream()
                .map(Product::of)
                .toList());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Product {

        private Long id;
        private String name;
        private long price;
        private int quantity;

        private Product(Long id, String name, long price, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public static Product of(ProductResult.Product product) {
            return new Product(product.getProductId(), product.getProductName(),
                product.getProductPrice(), product.getQuantity());
        }
    }

}
