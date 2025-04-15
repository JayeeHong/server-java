package kr.hhplus.be.server.application.product;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import kr.hhplus.be.server.interfaces.product.ProductResponse.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 전체 상품 목록을 조회한다.
     */
    public List<ProductDto> getAllProducts() {
        return ProductResponse.translate(productRepository.findAll());
    }

    @Transactional
    public List<Product> getAndDecreaseStock(List<OrderRequest.Item> items) {
        List<Long> productIds = items.stream()
            .map(OrderRequest.Item::getProductId)
            .toList();

        List<Product> products = productRepository.findAllByIdIn(productIds);

        Map<Long, Integer> quantityMap = items.stream()
            .collect(Collectors.toMap(OrderRequest.Item::getProductId, OrderRequest.Item::getQuantity));

        List<Product> updatedProducts = new ArrayList<>();
        for (Product product : products) {
            int orderQty = quantityMap.get(product.getId());
            if (product.getStock() < orderQty) {
                throw new IllegalStateException("재고가 부족합니다.");
            }
            Product decreased = product.decreaseStock(orderQty);
            productRepository.save(decreased);  // 즉시 반영
            updatedProducts.add(decreased);
        }

        return updatedProducts;
    }

    public int getPrice(Long productId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        return product.getPrice();
    }

}
