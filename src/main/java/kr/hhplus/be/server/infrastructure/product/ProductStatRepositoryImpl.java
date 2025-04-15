package kr.hhplus.be.server.infrastructure.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.PopularProduct;
import kr.hhplus.be.server.domain.product.ProductStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStatRepositoryImpl implements ProductStatRepository {

    @Override
    public List<PopularProduct> findTop5PopularProducts() {
        return List.of();
    }
}
