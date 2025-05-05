package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.ProductResult.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController implements ProductApi {

    private final ProductFacade productFacade;

    @Override
    public ProductResponse.Products getProducts() {
        Products products = productFacade.getProducts();
        return ProductResponse.Products.of(products);
    }
}
