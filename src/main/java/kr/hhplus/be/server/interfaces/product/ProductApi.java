package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "상품 API", description = "상품 관련 API입니다.")
public interface ProductApi {

    @Operation(summary = "전체 상품 목록 조회")
    ProductResponse.Products getProducts();

}
