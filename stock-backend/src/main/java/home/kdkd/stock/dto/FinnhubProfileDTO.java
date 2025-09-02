package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinnhubProfileDTO {
  @JsonProperty("finnhubIndustry")
  private String industry;
  private Double marketCapitalization;
  @JsonProperty("ticker")
  private String symbol;
}
