package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.ProductStatRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductStatService {

    private final ProductStatRepository productStatRepository;

    /**
     * 최근 3일간 가장 많이 팔린 상품 조회
     */
    public List<ProductResponse.HotProduct> getPopularProducts() {
        return productStatRepository.findTop5PopularProducts().stream()
            .map(product -> new ProductResponse.HotProduct(
                product.productId(),
                product.name(),
                product.totalSold()
            ))
            .toList();
    }
}
