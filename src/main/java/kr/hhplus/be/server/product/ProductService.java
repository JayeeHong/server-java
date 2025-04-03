package kr.hhplus.be.server.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    public List<Product> getProducts() {

        // 전체 상품 목록 조회

        return List.of(
            new Product(1L, "상품A", 1000, 100),
            new Product(2L, "상품B", 2000, 200)
        );
    }

    public Product getProduct(long productId) {

        // 상품 id 기준 목록 조회

        return new Product(1L, "상품A", 1000, 100);
    }

    public List<Product> getTop5Products() {

        // 전체 상품 중 상위 상품 5개 조회

        return List.of(
            new Product(5L, "상품E", 5000, 500),
            new Product(4L, "상품D", 4000, 400),
            new Product(3L, "상품C", 3000, 300),
            new Product(2L, "상품B", 2000, 200),
            new Product(1L, "상품A", 1000, 100)
        );
    }

}
