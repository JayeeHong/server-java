package kr.hhplus.be.server.product;

public record Product(
    long id,
    String name,
    int price,
    int stock
) {

}
