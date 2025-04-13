package kr.hhplus.be.server.domain.product;

/**
 * 인기 상품 정보 (조회 전용 projection)
 */
public record PopularProduct(
    Long productId,
    String name,
    int totalSold
) {
}
