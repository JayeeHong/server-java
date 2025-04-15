package kr.hhplus.be.server.integration.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Command;
import kr.hhplus.be.server.interfaces.order.OrderRequest.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/kr/hhplus/be/server/integration/order/order_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("통합 테스트 - 주문 성공")
    void placeOrder_success() throws Exception {

        // given
        OrderRequest.Command request = new Command(1L, null,
            List.of(new Item(101L, 2)));

        // when & then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1L))
            .andExpect(jsonPath("$.items[0].productId").value(101L))
            .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

}
