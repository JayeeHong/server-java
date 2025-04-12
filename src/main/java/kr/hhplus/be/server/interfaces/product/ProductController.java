package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    @GetMapping
    public List<ProductResponse.Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
