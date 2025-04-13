package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Product 인기상품 API", description = "인기 상품 조회 관련 API입니다.")
public interface ProductStatApi {

    @Operation(summary = "인기 상품 조회", description = "최근 3일간 가장 많이 팔린 인기 상품 목록을 조회합니다.")
    List<ProductResponse.HotProduct> getPopularProducts();
}
