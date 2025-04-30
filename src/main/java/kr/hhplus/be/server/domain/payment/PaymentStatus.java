package kr.hhplus.be.server.domain.payment;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    READY("결제 준비"),
    WAIT("결제 대기"),
    COMPLETE("결제 완료"),
    FAIL("결제 실패"),
    CANCEL("결제 취소"),
    ;

    private final String description;

    private static final List<PaymentStatus> CANNOT_PAY_STATUSES = List.of(COMPLETE, FAIL, CANCEL);

    public boolean cannotPay() {
        return CANNOT_PAY_STATUSES.contains(this);
    }

    public static List<PaymentStatus> forCompleted() {
        return List.of(COMPLETE);
    }

}
