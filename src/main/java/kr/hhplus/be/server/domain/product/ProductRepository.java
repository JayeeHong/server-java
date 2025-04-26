package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface ProductRepository {

    // 전체 상품 목록 조회
    List<Product> findAll();

    // 상품 ID로 단건 조회
    Product findById(Long productId);

    // 상품 ID로 단건 조회 (비관적 락 적용)
    Product findByIdWithPessimisticLock(Long productId);

    // 여러 상품 ID로 상품 목록 조회
    List<Product> findAllByIdIn(List<Long> productIds);

    // 상품 저장
    Product save(Product product);

    // 상품 삭제
    void deleteById(Long id);

}
