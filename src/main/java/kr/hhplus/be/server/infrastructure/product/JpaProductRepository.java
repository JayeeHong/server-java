package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

}
