package kr.hhplus.be.server.interfaces.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.product.ProductStatService;
import kr.hhplus.be.server.interfaces.product.ProductResponse.HotProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductStatController.class)
class ProductStatControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductStatService productStatService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("인기 상품 목록을 성공적으로 조회한다")
    void getPopularProducts_success() throws Exception {
        // given
        List<HotProduct> products = List.of(
            new HotProduct(1L, "콜드브루", 120),
            new HotProduct(2L, "아메리카노", 100)
        );
        when(productStatService.getPopularProducts()).thenReturn(products);

        // when & then
        mockMvc.perform(get("/api/v1/products/popular")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].productName", is("콜드브루")))
            .andExpect(jsonPath("$[0].totalSold", is(120)));
    }

    @Test
    @DisplayName("인기 상품이 없으면 빈 리스트를 반환한다")
    void getPopularProducts_empty() throws Exception {
        when(productStatService.getPopularProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products/popular")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(0)));
    }
}
