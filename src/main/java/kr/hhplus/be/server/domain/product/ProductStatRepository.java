package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface ProductStatRepository {

    /**
     * 최근 3일간 가장 많이 팔린 상품 TOP 5
     */
    List<PopularProduct> findTop5PopularProducts();
}
