package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooQuoteDTO {
    private double[] open;
    private double[] close;
    private double[] high;
    private double[] low;
    private long[] volume;
}
