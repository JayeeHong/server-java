//
//package kr.hhplus.be.server.interfaces.order;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.hhplus.be.server.application.order.OrderFacade;
//import kr.hhplus.be.server.interfaces.order.OrderRequest.Command;
//import kr.hhplus.be.server.interfaces.order.OrderRequest.Item;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//class OrderControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    OrderFacade orderFacade;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("정상 주문 시 200 반환")
//    void placeOrder_success() throws Exception {
//        Command request = new Command(1L, null, List.of(new Item(101L, 2)));
//
//        mockMvc.perform(post("/api/v1/order")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("잘못된 요청 시 400 반환")
//    void placeOrder_invalidRequest_returns400() throws Exception {
//        Command invalidRequest = new Command(null, null, List.of());
//
//        mockMvc.perform(post("/api/v1/order")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(invalidRequest)))
//            .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("잔액 부족 시 500 에러 반환")
//    void placeOrder_insufficientBalance_returns500() throws Exception {
//        Command request = new Command(1L, null, List.of(new Item(101L, 2)));
//
//        doThrow(new IllegalStateException("잔액이 부족합니다."))
//            .when(orderFacade).placeOrder(eq(request));
//
//        mockMvc.perform(post("/api/v1/order")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isInternalServerError())
//            .andExpect(content().string("잔액이 부족합니다."));
//    }
//
//    @Test
//    @DisplayName("유효하지 않은 쿠폰 시 400 반환")
//    void placeOrder_invalidCoupon_returns400() throws Exception {
//        Command request = new Command(1L, 99L, List.of(new Item(101L, 1)));
//
//        doThrow(new IllegalArgumentException("유효하지 않은 쿠폰입니다."))
//            .when(orderFacade).placeOrder(eq(request));
//
//        mockMvc.perform(post("/api/v1/order")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest())
//            .andExpect(content().string("유효하지 않은 쿠폰입니다."));
//    }
//
//    @Test
//    @DisplayName("재고 부족 시 500 에러 반환")
//    void placeOrder_stockFail_returns500() throws Exception {
//        Command request = new Command(1L, null, List.of(new Item(999L, 10)));
//
//        doThrow(new IllegalStateException("재고가 부족합니다."))
//            .when(orderFacade).placeOrder(eq(request));
//
//        mockMvc.perform(post("/api/v1/order")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isInternalServerError())
//            .andExpect(content().string("재고가 부족합니다."));
//    }
//}
