package kr.hhplus.be.server.infrastructure.rank;

import java.util.List;
import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.rank.RankCommand;
import kr.hhplus.be.server.domain.rank.RankInfo;
import kr.hhplus.be.server.domain.rank.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankRepositoryImpl implements RankRepository {

    private final RankJpaRepository rankJpaRepository;
    private final RankQueryDslRepository rankQueryDslRepository;

    @Override
    public Rank save(Rank rank) {
        return rankJpaRepository.save(rank);
    }

    @Override
    public List<RankInfo.PopularProduct> findPopularSellRanks(RankCommand.PopularSellRank command) {
        return rankQueryDslRepository.findPopularSellRanks(command);
    }
}
