package kr.hhplus.be.server.domain.balance;

public enum TransactionType {
    CHARGE,     // 충전
    PAYMENT     // 결제(차감)
    ;

    public boolean isCharge() {
        return this == CHARGE;
    }

    public boolean isPayment() {
        return this == PAYMENT;
    }
}
