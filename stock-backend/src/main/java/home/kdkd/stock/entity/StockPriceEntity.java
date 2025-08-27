package home.kdkd.stock.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class StockPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("c")
    private Double price;
    @Column(name = "percent_change")
    @JsonProperty("dp")
    private Long percentChange;
    @JsonProperty("t")
    private Long timestamp;
    @ManyToOne
    @JoinColumn(name="symbol", nullable=false)
    private StockInfoEntity stockEntity;
}
