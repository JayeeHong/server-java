package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.ProductStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductStatController implements ProductStatApi {

    private final ProductStatService productStatService;

    @GetMapping("/popular")
    public List<ProductResponse.HotProduct> getPopularProducts() {
        return productStatService.getPopularProducts();
    }
}
