package kr.hhplus.be.server.interfaces.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.hhplus.be.server.application.user.UserCouponFacade;
import kr.hhplus.be.server.application.user.UserCouponResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class UserCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    private UserCouponFacade userCouponFacade;

    @Test
    @DisplayName("사용자가 보유한 쿠폰 목록을 조회한다")
    void getCoupons() throws Exception {

        // given
        when(userCouponFacade.getUserCoupons(1L))
            .thenReturn(UserCouponResult.Coupons.of(
                List.of(
                    UserCouponResult.Coupon.of(1L, "couponA", 1000)
                )
            ));

        // when, then
        mockMvc.perform(get("/api/v1/users/{id}/coupons", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.coupons[*].id").value(1))
            .andExpect(jsonPath("$.data.coupons[*].name").value("couponA"))
            .andExpect(jsonPath("$.data.coupons[*].discountAmount").value(1000));
    }

    @DisplayName("쿠폰을 발급 시, 쿠폰 ID는 필수이다.")
    @Test
    void publishCouponWithoutCouponId() throws Exception {

        // given
        UserCouponRequest.Publish request = new UserCouponRequest.Publish();

        // when, then
        mockMvc.perform(
                post("/api/v1/users/{id}/coupons/publish", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("쿠폰 ID는 필수입니다."));
    }

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void publishCoupon() throws Exception {

        // given
        UserCouponRequest.Publish request = UserCouponRequest.Publish.of(1L);

        // when, then
        mockMvc.perform(
                post("/api/v1/users/{id}/coupons/publish", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}