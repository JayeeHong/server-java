package kr.hhplus.be.server.application.product;

import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.application.product.ProductResult.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;

    public ProductResult.Products getProducts() {
        ProductInfo.Products products = productService.getSellingProducts();

        List<Product> results = new ArrayList<>();
        products.getProducts().forEach(product ->
            results.add(ProductResult.Product.of(
                product.getProductId(), product.getProductName(), product.getProductPrice(), product.getQuantity())));

        return ProductResult.Products.of(results);
    }

}
