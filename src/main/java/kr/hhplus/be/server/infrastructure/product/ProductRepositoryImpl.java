package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Product findById(Long productId) {
        return productJpaRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Override
    public List<Product> findByStatusIn(List<ProductStatus> statuses) {
        return productJpaRepository.findByStatusIn(statuses);
    }

    @Override
    public Product findByIdWithPessimisticLock(Long productId) {
        return productJpaRepository.findByIdWithPessimisticLock(productId);
    }
}
