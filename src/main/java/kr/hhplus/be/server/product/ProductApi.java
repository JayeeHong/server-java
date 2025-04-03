package kr.hhplus.be.server.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Product", description = "상품 관련 API")
@RequestMapping("/api/v1/product")
public interface ProductApi {

    @Operation(summary = "전체 상품 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<Product> getProducts();

    @Operation(summary = "상품 상세 조회")
    @Parameter(name = "productId", description = "사용자 ID", required = true)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Product getProduct(@PathVariable long productId);

    @Operation(summary = "Top 5 인기 상품 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
        @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<Product> getTop5Products();

}
