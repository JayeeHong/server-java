package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;

public interface ProductRepository {

    /**
     * 전체 상품 목록 조회
     */
    List<Product> findAll();

    /**
     * 상품 ID로 단건 조회
     */
    Product findById(Long productId);
}
