package kr.hhplus.be.server.application.product;

import java.util.List;
import kr.hhplus.be.server.domain.common.Price;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse.Product> getProducts() {

        return productRepository.findAll().stream()
            .map(Product::translateProduct)
            .map(product -> new ProductResponse.Product(
                product.id(), product.name(), product.price(), product.stock()
            ))
            .toList();
    }

    public ProductResponse.Product getProduct(long productId) {

        Product findProduct = productRepository.findById(productId);
        if (findProduct == null) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        return findProduct.translateProduct();
    }

    public ProductResponse.Product restockProduct(long productId, int quantity) {
        // 상품 식별자 유효성 체크
        Product findProduct = productRepository.findById(productId);
        if (findProduct == null) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        findProduct.restock(quantity);

        Product product = productRepository.updateStock(productId, findProduct.getStock().quantity());

        return product.translateProduct();
    }

    public ProductResponse.Product purchaseProduct(long productId, int quantity) {
        // 상품 식별자 유효성 체크
        Product findProduct = productRepository.findById(productId);
        if (findProduct == null) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        findProduct.purchase(quantity);

        Product product = productRepository.updateStock(productId, findProduct.getStock().quantity());

        return product.translateProduct();
    }

    public ProductResponse.Product changeProductPrice(long productId, int price) {
        // 상품 식별자 유효성 체크
        Product findProduct = productRepository.findById(productId);
        if (findProduct == null) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        findProduct.changePrice(new Price(price));

        Product product = productRepository.updateStock(productId, findProduct.getStock().quantity());

        return product.translateProduct();
    }

    public List<ProductResponse.Product> getTop5Products() {

        // 전체 상품 중 상위 상품 5개 조회
        return productRepository.findTop5().stream()
            .map(Product::translateProduct)
            .map(product -> new ProductResponse.Product(
                product.id(), product.name(), product.price(), product.stock()
            ))
            .toList();
    }

}
