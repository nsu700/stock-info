package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuoteDTO {
    @JsonProperty("c")
    private Double price;
    @JsonProperty("dp")
    private Long percentChange;
}
