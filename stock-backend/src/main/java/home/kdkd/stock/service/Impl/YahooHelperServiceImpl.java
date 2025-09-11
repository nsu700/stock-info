package home.kdkd.stock.service.Impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import home.kdkd.stock.dto.YahooQuoteDTO;
import home.kdkd.stock.dto.YahooResponseDTO;
import home.kdkd.stock.dto.YahooResultDTO;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.repository.StockOHLCRepository;
import home.kdkd.stock.service.YahooHelperService;

@Service
public class YahooHelperServiceImpl implements YahooHelperService{
    @Autowired
    private StockOHLCRepository stockOHLCRepository;

    @Value("${conf.yahoo.host}")
    private String yahooHost;

    @Value("${conf.protocol}")
    private String protocol; 
    
    public YahooResponseDTO getYahooResponse(String symbol, long period1, long period2) {
        String url = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(yahooHost)
            .path("/{symbol}")
            .queryParam("period1", period1)
            .queryParam("period2", period2)
            .queryParam("interval", "1d")
            .queryParam("events", "history")
            .buildAndExpand(symbol)
            .toString();
        System.out.println("Querying yahoo api at " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        YahooResponseDTO yahooResponseDTO = new RestTemplate().exchange(
            url,
            HttpMethod.GET,
            entity,
            YahooResponseDTO.class).getBody();
        return yahooResponseDTO;
    }

    @Override
    public void processAndSaveYahooData(String symbol, long period1, long period2) {
        YahooResponseDTO yahooResponseDTO = this.getYahooResponse(symbol, period1, period2);
        YahooResultDTO yahooResultDTO = yahooResponseDTO.getYahooChartDTO().getYahooResultDTOs()[0];
        long[] timestamps = yahooResultDTO.getTimestamp();
        YahooQuoteDTO yahooQuoteDTO = yahooResultDTO.getYahooIndicatorsDTO().getYahooQuoteDTOs()[0];

        List<StockOHLCEntity> stockOHLCEntitys = new ArrayList<>();

        for (int i = 0; i < timestamps.length; i++) {
            StockOHLCEntity stockOHLCEntity = new StockOHLCEntity();
            stockOHLCEntity.setSymbol(symbol);
            stockOHLCEntity.setTimestamp(Instant.ofEpochSecond(timestamps[i]));
            stockOHLCEntity.setOpenPrice(BigDecimal.valueOf(yahooQuoteDTO.getOpen()[i]));
            stockOHLCEntity.setHighPrice(BigDecimal.valueOf(yahooQuoteDTO.getHigh()[i]));
            stockOHLCEntity.setLowPrice(BigDecimal.valueOf(yahooQuoteDTO.getLow()[i]));
            stockOHLCEntity.setClosePrice(BigDecimal.valueOf(yahooQuoteDTO.getClose()[i]));
            stockOHLCEntity.setVolume(yahooQuoteDTO.getVolume()[i]);
            
            stockOHLCEntitys.add(stockOHLCEntity);
        }

        stockOHLCRepository.saveAll(stockOHLCEntitys);
    }

}
