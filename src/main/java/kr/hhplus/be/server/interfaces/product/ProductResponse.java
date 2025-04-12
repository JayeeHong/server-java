package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ProductResponse {

    @Getter
    @RequiredArgsConstructor
    @Schema(description = "상품 응답 DTO")
    public static class Product {

        @Schema(description = "상품 ID", example = "1")
        private final Long id;

        @Schema(description = "상품 이름", example = "고급 노트북")
        private final String name;

        @Schema(description = "가격", example = "1500000")
        private final int price;

        @Schema(description = "남은 재고 수량", example = "8")
        private final int stock;
    }

    public static List<Product> translate(List<kr.hhplus.be.server.domain.product.Product> products) {
        return products.stream()
            .map(p -> new Product(p.id(), p.name(), p.price(), p.stock()))
            .toList();
    }

    public static Product from(kr.hhplus.be.server.domain.product.Product product) {
        return new Product(product.id(), product.name(), product.price(), product.stock());
    }
}
