//package kr.hhplus.be.server.interfaces.product;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.hhplus.be.server.application.product.ProductService;
//import kr.hhplus.be.server.interfaces.product.ProductResponse.ProductDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//class ProductControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    ProductService productService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("상품 목록 조회 API는 성공 시 200과 JSON 응답을 반환한다")
//    void getAllProducts_success() throws Exception {
//        List<ProductDto> products = List.of(
//            new ProductDto(1L, "상품A", 1000, 10),
//            new ProductDto(2L, "상품B", 2000, 5)
//        );
//
//        when(productService.getAllProducts()).thenReturn(products);
//
//        mockMvc.perform(get("/api/v1/products"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.length()").value(2))
//            .andExpect(jsonPath("$[0].name").value("상품A"))
//            .andExpect(jsonPath("$[1].price").value(2000));
//    }
//
//    @Test
//    @DisplayName("상품 조회 중 예외 발생 시 500 에러를 반환한다")
//    void getAllProducts_internalError() throws Exception {
//        when(productService.getAllProducts()).thenThrow(new RuntimeException("DB 에러"));
//
//        mockMvc.perform(get("/api/v1/products"))
//            .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("상품 조회 중 잘못된 요청이 발생하면 400 에러를 반환한다")
//    void getAllProducts_illegalArgument_returns400() throws Exception {
//        when(productService.getAllProducts()).thenThrow(new IllegalArgumentException("쿼리 파라미터가 잘못됨"));
//
//        mockMvc.perform(get("/api/v1/products"))
//            .andExpect(status().isBadRequest())
//            .andExpect(content().string(org.hamcrest.Matchers.containsString("잘못된 요청")));
//    }
//
//}
