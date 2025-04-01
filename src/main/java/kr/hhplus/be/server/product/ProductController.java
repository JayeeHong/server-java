package kr.hhplus.be.server.product;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @GetMapping("/list")
    public List<Product> getProducts() {
        return List.of(
            new Product(1L, "상품A", 1000, 100),
            new Product(2L, "상품B", 2000, 200)
        );
    }

    @GetMapping("/detail/{productId}")
    public Product getProduct(@PathVariable long productId) {
        return new Product(1L, "상품A", 1000, 100);
    }

    @GetMapping("/top5")
    public List<Product> getTop5Products() {
        return List.of(
            new Product(5L, "상품E", 5000, 500),
            new Product(4L, "상품D", 4000, 400),
            new Product(3L, "상품C", 3000, 300),
            new Product(2L, "상품B", 2000, 200),
            new Product(1L, "상품A", 1000, 100)
        );
    }

}
