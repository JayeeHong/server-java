package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.common.Price;
import kr.hhplus.be.server.domain.common.Stock;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.Getter;

@Getter
public class Product {
    private final long id;
    private String name;
    private Price price;
    private Stock stock;

    public Product(long id, String name, Price price, Stock stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void purchase(int quantity) {
        if (stock.quantity() < quantity) {
            throw new IllegalArgumentException("구매 실패: 재고 부족");
        }
        this.stock = stock.decrease(quantity);
    }

    public void restock(int quantity) {
        this.stock = stock.increase(quantity);
    }

    public void changePrice(Price newPrice) {
        this.price = newPrice;
    }

    public ProductResponse.Product translateProduct() {
        return new ProductResponse.Product(this.id, this.name, this.price.value(), this.stock.quantity());
    }

    public static List<ProductResponse.Product> translateProducts(List<Product> products) {
        return products.stream()
            .map(Product::translateProduct)
            .collect(Collectors.toList());
    }

}
