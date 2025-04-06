package kr.hhplus.be.server.domain.product;

public record Product(
    long id,
    String name,
    int price,
    int stock
) {

}
