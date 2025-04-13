
package kr.hhplus.be.server.interfaces.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.coupon.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("쿠폰 발급 API는 성공 시 200과 응답 바디를 반환한다")
    void issueCoupon_success() throws Exception {
        long couponId = 10L;
        long userId = 1L;
        CouponResponse.UserCoupon response = new CouponResponse.UserCoupon(
            couponId, "테스트쿠폰", 1000, LocalDateTime.now()
        );

        when(couponService.issueCoupon(userId, couponId)).thenReturn(response);

        mockMvc.perform(post("/api/v1/coupon/{couponId}/issue/{userId}", couponId, userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.couponId").value(couponId))
            .andExpect(jsonPath("$.couponName").value("테스트쿠폰"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 또는 쿠폰으로 발급 시 400 에러를 반환한다")
    void issueCoupon_invalid_returns400() throws Exception {
        long couponId = 99L;
        long userId = 99L;

        when(couponService.issueCoupon(userId, couponId))
            .thenThrow(new IllegalArgumentException("쿠폰 또는 사용자 정보가 유효하지 않습니다."));

        mockMvc.perform(post("/api/v1/coupon/{couponId}/issue/{userId}", couponId, userId))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("쿠폰 또는 사용자 정보가 유효하지 않습니다")));
    }

    @Test
    @DisplayName("보유 쿠폰 목록 조회 API는 성공 시 200과 JSON 배열을 반환한다")
    void getUserCoupons_success() throws Exception {
        long userId = 1L;
        CouponResponse.UserCoupon coupon = new CouponResponse.UserCoupon(
            1L, "테스트쿠폰", 1000, LocalDateTime.now()
        );

        when(couponService.getUserCoupons(userId)).thenReturn(List.of(coupon));

        mockMvc.perform(get("/api/v1/coupon/{userId}/list", userId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].couponName").value("테스트쿠폰"));
    }
}
