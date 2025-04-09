package kr.hhplus.be.server.application.coupon;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCode;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.Discount;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.infrastructure.coupon.CouponRepository;
import kr.hhplus.be.server.infrastructure.user.UserCouponRepository;
import kr.hhplus.be.server.infrastructure.user.UserRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CouponServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private CouponService couponService;

    private final long USER_ID = 1L;
    private final long COUPON_ID = 100L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 사용자_없으면_예외_발생() {
        when(userRepository.findById(USER_ID)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> couponService.issueCoupon(USER_ID, COUPON_ID));

        assertEquals("유효하지 않는 사용자입니다.", exception.getMessage());
    }

    @Test
    void 쿠폰_없으면_예외_발생() {
        when(userRepository.findById(USER_ID)).thenReturn(new User(USER_ID));
        when(couponRepository.findById(COUPON_ID)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> couponService.issueCoupon(USER_ID, COUPON_ID));

        assertEquals("유효하지 않는 쿠폰입니다.", exception.getMessage());
    }

    @Test
    void 쿠폰_정상_발급() {
        // given
        User user = new User(USER_ID);
        Coupon coupon = mock(Coupon.class);
        CouponCode code = mock(CouponCode.class);
        Discount discount = mock(Discount.class);

        // coupon.getCode().getValue() 호출 대비
        when(coupon.getCode()).thenReturn(code);
        when(code.getValue()).thenReturn("SPRING10");
        when(coupon.getDiscount()).thenReturn(discount);
        when(discount.getAmount()).thenReturn(100);  // 예시 금액

        // 기타 필드들 (translateUserCoupon 내부에서 참조할 수 있음)
        when(coupon.getType()).thenReturn(CouponType.PERCENTAGE); // 필요 없을 수도 있음
        doNothing().when(coupon).issue();

        // 발급된 쿠폰과 응답 객체 mock
        UserCoupon issuedCoupon = mock(UserCoupon.class);
        CouponResponse.UserCoupon response = mock(CouponResponse.UserCoupon.class);
        when(issuedCoupon.translateUserCoupon()).thenReturn(response);

        // repository stubbing
        when(userRepository.findById(USER_ID)).thenReturn(user);
        when(couponRepository.findById(COUPON_ID)).thenReturn(coupon);
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(issuedCoupon);

        // when
        CouponResponse.UserCoupon result = couponService.issueCoupon(USER_ID, COUPON_ID);

        // then
        assertEquals("SPRING10", result.couponCode());
        assertEquals(100, result.discount());
        assertEquals(CouponType.PERCENTAGE, result.type());
        assertFalse(result.used());

        verify(coupon).issue();
        verify(couponRepository).save(issuedCoupon);
    }
}