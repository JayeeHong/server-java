package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.common.Price;
import kr.hhplus.be.server.domain.common.Stock;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<Product> findAll() {

        return List.of(
            new Product(1L, "상품A", new Price(1000), new Stock(100)),
            new Product(2L, "상품B", new Price(2000), new Stock(200))
        );
    }

    @Override
    public Product findById(long productId) {

        return new Product(productId, "상품A", new Price(1000), new Stock(100));
    }

    @Override
    public List<Product> findTop5() {

        return List.of(
            new Product(5L, "상품E", new Price(5000), new Stock(500)),
            new Product(4L, "상품D", new Price(4000), new Stock(400)),
            new Product(3L, "상품C", new Price(3000), new Stock(300)),
            new Product(2L, "상품B", new Price(2000), new Stock(200)),
            new Product(1L, "상품A", new Price(1000), new Stock(100))
        );
    }

    @Override
    public Product updateStock(long productId, int stock) {

        return new Product(productId, "상품A", new Price(1000), new Stock(stock));
    }

    @Override
    public Product updatePrice(long productId, int price) {

        return new Product(productId, "상품A", new Price(price), new Stock(100));
    }
}
