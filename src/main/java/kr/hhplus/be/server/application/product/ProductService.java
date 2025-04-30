package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.config.redis.DistributedLock;
import kr.hhplus.be.server.config.redis.RedissonLockManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import kr.hhplus.be.server.interfaces.product.ProductResponse.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final RedissonLockManager lockService;

    /**
     * 전체 상품 목록을 조회한다.
     */
    public List<ProductDto> getAllProducts() {
        return ProductResponse.translate(productRepository.findAll());
    }

    @Transactional
    public List<Product> getAndDecreaseStock(List<OrderRequest.Item> items) {
        List<Long> productIds = items.stream()
            .map(OrderRequest.Item::getProductId)
            .toList();

        List<Product> products = productRepository.findAllByIdIn(productIds);

        Map<Long, Integer> quantityMap = items.stream()
            .collect(Collectors.toMap(OrderRequest.Item::getProductId, OrderRequest.Item::getQuantity));

        List<Product> updatedProducts = new ArrayList<>();
        for (Product product : products) {
            int orderQty = quantityMap.get(product.getId());
            if (product.getStock() < orderQty) {
                throw new IllegalStateException("재고가 부족합니다.");
            }
            product.decreaseStock(orderQty);
            updatedProducts.add(product);
        }

        return updatedProducts;
    }

    @Cacheable(cacheNames = "product", key = "#productId")
    @Transactional(readOnly = true)
    public Product getProductInfo(Long productId) {
        return productRepository.findById(productId);
    }

    @CachePut(cacheNames = "product", key = "#productId")
    @Transactional
    @DistributedLock(key = "'product:' + #productId")
    public Product decreaseStockWithRedisson(Long productId, int quantity) {

        Product product = productRepository.findById(productId);
        product.decreaseStock(quantity);

        return product;
    }

    @Retryable(
        value = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
        maxAttempts = 3,  // 최대 3번 재시도
        backoff = @Backoff(delay = 100) // 100ms 쉬고 재시도
    )
    @Transactional
    public void decreaseStockWithOptimisticLock(Long productId, int quantity) {
        Product product = productRepository.findById(productId);

        product.decreaseStock(quantity);
    }

    @Transactional
    public void decreaseStockWithPessimisticLock(Long productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId);

        product.decreaseStock(quantity);
    }

    @Transactional
    public void decreaseStockWithoutConcurrency(Long productId, int quantity) {
        Product product = productRepository.findById(productId);

        product.decreaseStock(quantity);
    }

    @Transactional(readOnly = true)
    public int getPrice(Long productId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        return product.getPrice();
    }

    @CacheEvict(cacheNames = "product", key = "#productId")
    @Transactional
    public Long deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        return productId;
    }

}
