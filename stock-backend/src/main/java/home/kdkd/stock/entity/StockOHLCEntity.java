package home.kdkd.stock.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_ohlc_data")
@Setter
@Getter
@NoArgsConstructor
public class StockOHLCEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @Column(name = "timestamp_utc")
    private Instant timestamp; 
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal adjClosePrice;
    private Long volume;
    // @ManyToOne
    // @JoinColumn(name = "stock_info")
    // private StockInfoEntity stockInfoEntity;
}
