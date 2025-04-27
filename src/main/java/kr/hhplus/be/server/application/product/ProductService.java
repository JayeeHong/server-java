package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.config.redis.RedissonLockService;
import kr.hhplus.be.server.config.redis.RedissonResultDto;
import lombok.extern.slf4j.Slf4j;
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
    private final RedissonLockService lockService;

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

    @Transactional
    public RedissonResultDto decreaseStockWithRedisson(Long productId, int quantity) {

        boolean locked = lockService.tryLock(String.valueOf(productId));

        if (!locked) {
            return RedissonResultDto.of("해당 상품 재고 감소 처리가 이미 진행 중입니다.", false);
        }

        try {
            log.info("상품 재고 감소 처리 시작: productId={}", productId);

            Product product = productRepository.findById(productId);
            product.decreaseStock(quantity);

            log.info("상품 재고 감소 처리 완료: productId={}", productId);

            return RedissonResultDto.of("상품 재고 감소 처리 완료", true);
        } catch (Exception e) {
            log.error("상품 재고 감소 중 에러 발생: {}", e.getMessage(), e);

            return RedissonResultDto.of("상품 재고 감소 처리 실패", false);
        } finally {
            lockService.unlock(String.valueOf(productId));
            log.info("상품 락 해제 완료: productId={}", productId);
        }
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

}
