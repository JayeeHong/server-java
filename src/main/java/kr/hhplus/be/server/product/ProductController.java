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
    public List<ProductDto> getProducts() {
        return List.of(
            new ProductDto(1L, "상품A", 1000, 100),
            new ProductDto(2L, "상품B", 2000, 200)
        );
    }

    @GetMapping("/detail/{productId}")
    public ProductDto getProduct(@PathVariable long productId) {
        return new ProductDto(1L, "상품A", 1000, 100);
    }

    @GetMapping("/top5")
    public List<ProductDto> getTop5Products() {
        return List.of(
            new ProductDto(5L, "상품E", 5000, 500),
            new ProductDto(4L, "상품D", 4000, 400),
            new ProductDto(3L, "상품C", 3000, 300),
            new ProductDto(2L, "상품B", 2000, 200),
            new ProductDto(1L, "상품A", 1000, 100)
        );
    }

}
