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

}
