package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ProductResponse {

    @Getter
    @RequiredArgsConstructor
    @Schema(description = "상품 응답 DTO")
    public static class ProductDto {

        @Schema(description = "상품 ID", example = "1")
        private final Long id;

        @Schema(description = "상품 이름", example = "고급 노트북")
        private final String name;

        @Schema(description = "가격", example = "1500000")
        private final long price;

        @Schema(description = "남은 재고 수량", example = "8")
        private final int stock;
    }

    @Schema(description = "인기 상품 정보")
    public record HotProduct(
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "상품명", example = "콜드브루")
        String productName,

        @Schema(description = "총 판매 수량", example = "123")
        int totalSold
    ) {}

    public static List<ProductDto> translate(List<kr.hhplus.be.server.domain.product.Product> products) {
        return products.stream()
            .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getQuantity()))
            .toList();
    }

    public static ProductDto from(kr.hhplus.be.server.domain.product.Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }
}
