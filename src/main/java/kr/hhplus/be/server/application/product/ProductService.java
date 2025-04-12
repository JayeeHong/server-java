package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.infrastructure.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 전체 상품 목록을 조회한다.
     */
    public List<ProductResponse.Product> getAllProducts() {
        return ProductResponse.translate(productRepository.findAll());
    }
}
