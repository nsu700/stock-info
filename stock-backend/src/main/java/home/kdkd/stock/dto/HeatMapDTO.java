package home.kdkd.stock.dto;

import lombok.Builder;

@Builder
public class HeatMapDTO {
  private String symbol;
  private String industry;
  private double priceChangePercentage;
  private double marketCapitalization;
}
