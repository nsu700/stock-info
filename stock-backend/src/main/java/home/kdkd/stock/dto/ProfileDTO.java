package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileDTO {
  @JsonProperty("finnhubIndustry")
  private String industry;
  private Double marketCapitalization;
}
