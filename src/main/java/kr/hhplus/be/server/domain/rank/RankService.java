package kr.hhplus.be.server.domain.rank;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;

    public List<Rank> createSellRank(RankCommand.CreateList command) {
        return command.getRanks().stream()
            .map(this::createSell)
            .map(rankRepository::save)
            .toList();
    }

    public RankInfo.PopularProducts getPopularSellRank(RankCommand.PopularSellRank command) {
        List<RankInfo.PopularProduct> popularProducts = rankRepository.findPopularSellRanks(command);
        return RankInfo.PopularProducts.of(popularProducts);
    }

    private Rank createSell(RankCommand.Create command) {
        return Rank.createSell(command.getProductId(), command.getRankDate(), command.getScore());
    }
}
