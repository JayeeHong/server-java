package kr.hhplus.be.server.application.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    public List<ProductResponse.Product> getProducts() {

        // 전체 상품 목록 조회

        return List.of(
            new ProductResponse.Product(1L, "상품A", 1000, 100),
            new ProductResponse.Product(2L, "상품B", 2000, 200)
        );
    }

    public ProductResponse.Product getProduct(long productId) {

        // 상품 id 기준 목록 조회

        return new ProductResponse.Product(1L, "상품A", 1000, 100);
    }

    public List<ProductResponse.Product> getTop5Products() {

        // 전체 상품 중 상위 상품 5개 조회

        return List.of(
            new ProductResponse.Product(5L, "상품E", 5000, 500),
            new ProductResponse.Product(4L, "상품D", 4000, 400),
            new ProductResponse.Product(3L, "상품C", 3000, 300),
            new ProductResponse.Product(2L, "상품B", 2000, 200),
            new ProductResponse.Product(1L, "상품A", 1000, 100)
        );
    }

}
