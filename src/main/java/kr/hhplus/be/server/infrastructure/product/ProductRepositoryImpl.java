package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }

    @Override
    public Product findById(Long productId) {
        return jpaProductRepository.findById(productId).orElse(null);
    }

    @Override
    public Product findByIdWithPessimisticLock(Long productId) {
        return jpaProductRepository.findByIdWithPessimisticLock(productId);
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> productIds) {
        return jpaProductRepository.findAllById(productIds);
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public void deleteById(Long productId) {
        jpaProductRepository.deleteById(productId);
    }
}
