package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusIn(List<ProductStatus> statuses);

}
