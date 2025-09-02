package home.kdkd.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooResultDTO {
    private long[] timestamp;
    private YahooIndicatorsDTO yahooIndicatorsDTO;
    private YahooMetaDTO yahooMetaDTO;
}
