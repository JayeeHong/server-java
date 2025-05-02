package kr.hhplus.be.server.domain.product;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepository {

    Product save(Product product);

    Product findById(Long productId);

    List<Product> findByStatusIn(List<ProductStatus> statuses);

}
