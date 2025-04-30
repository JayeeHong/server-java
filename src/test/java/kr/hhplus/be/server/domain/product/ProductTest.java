package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Test
    @DisplayName("상품 생성 시 이름은 필수이다")
    void createWithInvalidName() {
        // when, then
        assertThatThrownBy(() -> Product.create(null, 1000, 10, ProductStatus.SELLING))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 금액은 0 보다 커야 한다")
    void createWithInvalidPrice() {
        // when, then
        assertThatThrownBy(() -> Product.create("productA", 0, 10, ProductStatus.SELLING))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 상품 판매 상태는 필수이다")
    void createWithInvalidStatus() {
        // when, then
        assertThatThrownBy(() -> Product.create("productA", 0, 10, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 수량은 0 이상이어야 한다")
    void createWithInvalidQuantity() {
        // when, then
        assertThatThrownBy(() -> Product.create("productA", 1000, -1, ProductStatus.HOLD))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"HOLD", "STOP_SELLING"})
    @DisplayName("상품 판매 상태 확인 - 판매 중이지 않음")
    void cannotSelling(ProductStatus status) {

        // given
        Product product = Product.create("productA", 1000, 100, status);

        // when
        boolean result = product.cannotSelling();

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"SELLING"})
    @DisplayName("상품 판매 상태 확인 - 판매 중임")
    void canSelling(ProductStatus status) {

        // given
        Product product = Product.create("productA", 1000, 100, status);

        // when
        boolean result = product.cannotSelling();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("상품 재고 차감 시 재고는 충분해야 한다")
    void decreaseWithInsufficientQuantity() {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);

        // when
        assertThatThrownBy(() -> product.decreaseStock(11))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("상품 재고를 차감한다")
    void decreaseStock() {

        // given
        Product product = Product.create("productA", 1000, 10, ProductStatus.SELLING);

        // when
        product.decreaseStock(1);

        // then
        assertThat(product.getQuantity()).isEqualTo(9);
    }
}
