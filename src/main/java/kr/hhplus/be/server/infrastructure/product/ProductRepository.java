package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;

public interface ProductRepository {

    List<Product> findAll();

    Product findById(long productId);

    List<Product> findTop5();

    Product updateStock(long productId, int stock);

    Product updatePrice(long productId, int price);
}
