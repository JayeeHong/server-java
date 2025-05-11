package kr.hhplus.be.server.interfaces.rank;

import kr.hhplus.be.server.application.rank.RankCriteria;
import kr.hhplus.be.server.application.rank.RankFacade;
import kr.hhplus.be.server.application.rank.RankResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RankController {

    private final RankFacade rankFacade;

    @GetMapping("/api/v1/products/ranks")
    public RankResponse.PopularProducts getPopularProducts() {
        RankResult.PopularProducts popularProducts = rankFacade.getPopularProducts(RankCriteria.PopularProducts.ofTop5Days3());
        return RankResponse.PopularProducts.of(popularProducts);
    }
}
