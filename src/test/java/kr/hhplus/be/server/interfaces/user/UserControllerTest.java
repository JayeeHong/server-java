package kr.hhplus.be.server.interfaces.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("잔액 충전 API는 성공 시 200과 응답 바디를 반환한다")
    void chargeBalance_success() throws Exception {
        Long userId = 1L;
        UserRequest.Charge request = new UserRequest.Charge(1000);
        UserResponse.Balance response = new UserResponse.Balance(userId, "홍길동", 5000);

        when(userService.chargeBalance(userId, request.getAmount())).thenReturn(response);

        mockMvc.perform(post("/api/v1/user/{userId}/charge", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andExpect(jsonPath("$.balance").value(5000));
    }

    @Test
    @DisplayName("잔액 조회 API는 성공 시 200과 응답 바디를 반환한다")
    void getBalance_success() throws Exception {
        Long userId = 1L;
        UserResponse.Balance response = new UserResponse.Balance(userId, "홍길동", 4000);

        when(userService.getUserBalance(userId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/user/{userId}/balance", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andExpect(jsonPath("$.balance").value(4000));
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 충전 시 400 에러를 반환한다")
    void chargeBalance_userNotFound_returns400() throws Exception {
        Long invalidUserId = 999L;
        UserRequest.Charge request = new UserRequest.Charge(1000);

        when(userService.chargeBalance(eq(invalidUserId), eq(request.getAmount())))
            .thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

        mockMvc.perform(post("/api/v1/user/{userId}/charge", invalidUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("존재하지 않는 사용자")));
    }

}
