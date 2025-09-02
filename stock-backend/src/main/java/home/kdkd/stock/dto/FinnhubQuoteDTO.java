package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinnhubQuoteDTO {
    @JsonProperty("c")
    private Double price;
    @JsonProperty("dp")
    private Double percentChange;
}
