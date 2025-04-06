package kr.hhplus.be.server.product;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 정보")
public record Product(
    @Schema(description = "상품 ID", example = "1001")
    long id,
    @Schema(description = "상품명", example = "맥북프로")
    String name,
    @Schema(description = "상품가격", example = "100000")
    int price,
    @Schema(description = "상품재고", example = "100")
    int stock
) {

}
