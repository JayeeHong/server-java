package kr.hhplus.be.server.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @Override
    public Product getProduct(@PathVariable long productId) {
        return productService.getProduct(productId);
    }

    @Override
    public List<Product> getTop5Products() {
        return productService.getTop5Products();
    }

}
