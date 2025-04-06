package kr.hhplus.be.server.domain.order;

public record OrderItem(
    long id,
    long orderId,
    long productId,
    int quantity,
    int price
) {

}
