package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    HOLD("판매 보류"),
    SELLING("판매 중"),
    STOP_SELLING("판매 중지"),
    ;

    private final String description;

    private static final List<ProductStatus> CANNOT_SELLING_STATUSES = List.of(HOLD, STOP_SELLING);

    public boolean cannotSelling() {
        return CANNOT_SELLING_STATUSES.contains(this);
    }

    public static List<ProductStatus> forSelling() {
        return List.of(SELLING);
    }

}
