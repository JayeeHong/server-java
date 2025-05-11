package kr.hhplus.be.server.infrastructure.rank;

import kr.hhplus.be.server.domain.rank.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankJpaRepository extends JpaRepository<Rank, Long> {
}
