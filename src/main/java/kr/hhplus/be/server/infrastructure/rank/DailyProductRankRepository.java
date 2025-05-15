package kr.hhplus.be.server.infrastructure.rank;


import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.rank.DailyProductRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyProductRankRepository extends JpaRepository<DailyProductRank, Long> {

    List<DailyProductRank> findByRankDateOrderByScoreDesc(LocalDate yesterday);

}
