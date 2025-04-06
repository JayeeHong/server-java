package kr.hhplus.be.server.product;

import java.util.List;
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
    public List<Product> getAllProducts() {
        return productService.getProducts();
    }

    @Override
    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable long productId) {
        return productService.getProduct(productId);
    }

    @Override
    @GetMapping("/top5")
    public List<Product> getTop5Products() {
        return productService.getTop5Products();
    }

}
