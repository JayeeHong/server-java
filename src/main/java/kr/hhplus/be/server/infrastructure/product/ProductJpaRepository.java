package kr.hhplus.be.server.infrastructure.product;

import jakarta.persistence.LockModeType;
import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusIn(List<ProductStatus> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p FROM Product p WHERE p.id = :productId")
    Product findByIdWithPessimisticLock(Long productId);

}
