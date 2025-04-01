package kr.hhplus.be.server.product;

public record ProductDto(
    long id,
    String name,
    int price,
    int stock
) {

}
