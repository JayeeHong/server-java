package kr.hhplus.be.server.interfaces.product;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.application.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    @GetMapping
    public List<ProductResponse.Product> getAllProducts() {
        return productService.getProducts();
    }

    @Override
    @GetMapping("/{productId}")
    public ProductResponse.Product getProduct(@PathVariable long productId) {
        return productService.getProduct(productId);
    }

    @Override
    @GetMapping("/top5")
    public List<ProductResponse.Product> getTop5Products() {
        return productService.getTop5Products();
    }

}
