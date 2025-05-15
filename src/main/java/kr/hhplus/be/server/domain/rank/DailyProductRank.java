package kr.hhplus.be.server.domain.rank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_product_rank")
public class DailyProductRank {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate rankDate;

    private Long productId;

    private Long score;

    @Builder
    private DailyProductRank(Long id, LocalDate rankDate, Long productId, Long score) {
        this.id = id;
        this.rankDate = rankDate;
        this.productId = productId;
        this.score = score;
    }
}
