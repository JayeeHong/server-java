package kr.hhplus.be.server.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderProducts;
import kr.hhplus.be.server.domain.product.ProductInfo.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문에 필요한 상품 정보 조회 시 주문 불가한 상품이 있다면 예외가 발생한다")
    void getOrderProductsWithNotSellingOrderProduct() {

        // given
        ProductCommand.Create sellProduct = ProductCommand.Create.of("productA", 1_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedSellProduct = productService.saveProduct(sellProduct);
        ProductCommand.Create stopProduct = ProductCommand.Create.of("productB", 2_000L, 100,
            ProductStatus.STOP_SELLING);
        ProductInfo.Product savedStopProduct = productService.saveProduct(stopProduct);
        ProductCommand.Create holdProduct = ProductCommand.Create.of("productC", 3_000L, 100,
            ProductStatus.HOLD);
        ProductInfo.Product savedHoldProduct = productService.saveProduct(holdProduct);

        List<ProductCommand.OrderProduct> orderProducts = List.of(
            ProductCommand.OrderProduct.of(savedSellProduct.getProductId(), 1),
            ProductCommand.OrderProduct.of(savedStopProduct.getProductId(), 1),
            ProductCommand.OrderProduct.of(savedHoldProduct.getProductId(), 1)
        );

        OrderProducts command = OrderProducts.of(orderProducts);

        // when, then
        assertThatThrownBy(() -> productService.getOrderProducts(command))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("주문에 필요한 상품을 조회한다")
    void getOrderProducts() {

        // given
        ProductCommand.Create productA = ProductCommand.Create.of("productA", 1_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductA = productService.saveProduct(productA);
        ProductCommand.Create productB = ProductCommand.Create.of("productB", 2_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductB = productService.saveProduct(productB);
        ProductCommand.Create productC = ProductCommand.Create.of("productC", 3_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductC = productService.saveProduct(productC);

        List<ProductCommand.OrderProduct> orderProducts = List.of(
            ProductCommand.OrderProduct.of(savedProductA.getProductId(), 1),
            ProductCommand.OrderProduct.of(savedProductB.getProductId(), 1),
            ProductCommand.OrderProduct.of(savedProductC.getProductId(), 1)
        );

        OrderProducts command = OrderProducts.of(orderProducts);

        // when
        ProductInfo.OrderProducts findOrderProducts = productService.getOrderProducts(command);

        // then
        assertThat(findOrderProducts.getOrderProducts()).hasSize(3);
    }

    @Test
    @DisplayName("판매중인 상품을 조회한다")
    void getSellStatusProducts() {

        // given
        ProductCommand.Create productA = ProductCommand.Create.of("productA", 1_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductA = productService.saveProduct(productA);

        // when
        Products sellingProducts = productService.getSellingProducts();

        // then
        assertThat(sellingProducts.getProducts()).hasSize(1);
    }

    @Test
    @DisplayName("상품 ID 리스트로 상품 정보를 조회한다")
    void getProductsByIds() {

        // given
        ProductCommand.Create productA = ProductCommand.Create.of("productA", 1_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductA = productService.saveProduct(productA);
        ProductCommand.Create productB = ProductCommand.Create.of("productB", 2_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductB = productService.saveProduct(productB);
        ProductCommand.Create productC = ProductCommand.Create.of("productC", 3_000L, 100,
            ProductStatus.SELLING);
        ProductInfo.Product savedProductC = productService.saveProduct(productC);

        ProductCommand.Products command = ProductCommand.Products.of(List.of(savedProductA.getProductId(), savedProductB.getProductId(), savedProductC.getProductId()));

        Products products = productService.getProducts(command);

        // then
        assertThat(products.getProducts()).hasSize(3)
            .extracting("productId", "productName", "productPrice")
            .containsExactly(
                tuple(savedProductA.getProductId(), savedProductA.getProductName(), savedProductA.getProductPrice()),
                tuple(savedProductB.getProductId(), savedProductB.getProductName(), savedProductB.getProductPrice()),
                tuple(savedProductC.getProductId(), savedProductC.getProductName(), savedProductC.getProductPrice())
            );
    }
}
