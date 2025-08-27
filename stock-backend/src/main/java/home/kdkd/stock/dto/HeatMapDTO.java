package home.kdkd.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HeatMapDTO {
  private String symbol;
  private String industry;
  private double priceChangePercentage;
  private double marketCapitalization;
}
