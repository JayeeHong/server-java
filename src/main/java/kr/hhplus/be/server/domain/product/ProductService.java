package kr.hhplus.be.server.domain.product;

import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderItem;
import kr.hhplus.be.server.domain.product.ProductCommand.OrderItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo.OrderItems getOrderProducts(OrderItems command) {

        List<ProductInfo.OrderItem> orderItems = new ArrayList<>();

        command.getOrderItems().forEach(pCommand -> {
            Product product = getSellingProduct(pCommand);
            ProductInfo.OrderItem productInfo = ProductCommand.Product.toOrderProductInfo(product, pCommand.getQuantity());
            orderItems.add(productInfo);
        });

        return ProductInfo.OrderItems.of(orderItems);
    }

    public ProductInfo.Products getSellingProducts() {
        List<Product> sellingProducts = productRepository.findByStatusIn(ProductStatus.forSelling());

        List<ProductInfo.Product> products = new ArrayList<>();

        sellingProducts.forEach(sellProduct -> {
            ProductInfo.Product productInfo = ProductCommand.Product.toProductInfo(sellProduct);
            products.add(productInfo);
        });

        return ProductInfo.Products.of(products);
    }

    public ProductInfo.Products getProducts(ProductCommand.Products command) {
        List<ProductInfo.Product> products = new ArrayList<>();

        command.getProductIds().forEach(productId -> {
            Product product = productRepository.findById(productId);
            ProductInfo.Product productInfo = ProductCommand.Product.toProductInfo(product);
            products.add(productInfo);
        });

        return ProductInfo.Products.of(products);
    }

    public Product getSellingProduct(OrderItem command) {
        Product product = productRepository.findById(command.getProductId());

        if (product.cannotSelling()) {
            throw new IllegalStateException("주문 불가한 상품이 포함되어 있습니다.");
        }

        return product;
    }

    public ProductInfo.Product saveProduct(ProductCommand.Create command) {
        Product product = Product.create(command.getName(), command.getPrice(),
            command.getQuantity(),
            command.getStatus());

        productRepository.save(product);

        return ProductInfo.Product.of(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    @Transactional
    public void decreaseStock(ProductCommand.OrderItems command) {
        command.getOrderItems().forEach(this::decreaseStock);
    }

    public void decreaseStock(ProductCommand.OrderItem command) {
        Product product = productRepository.findById(command.getProductId());
        product.decreaseStock(command.getQuantity());
    }

    public ProductInfo.Product getProduct(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductInfo.Product.of(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }
}
