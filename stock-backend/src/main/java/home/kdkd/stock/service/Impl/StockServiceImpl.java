package home.kdkd.stock.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;
import home.kdkd.stock.entity.StockInfoEntity;
import home.kdkd.stock.repository.StockInfoRepository;
import home.kdkd.stock.service.StockService;


@Service
public class StockServiceImpl implements StockService{
    @Autowired
    private StockInfoRepository stockInfoRepository;
    // @Value("${conf.finnhub.host}")
    // private String host;

    // @Value("${conf.protocol}")
    // private String protocol; 

    // @Value("${conf.key}")
    // private String key;

    // @Value("${conf.finnhub.endpoints.quote}")
    // private String quoteEndpoint;

    // @Value("${conf.finnhub.endpoints.profile}")
    // private String profileEndpoint;

    final private String host = "finnhub.io/api/v1";
    final private String protocol = "https";
    final private String quoteEndpoint = "/quote";
    final private String profileEndpoint = "/stock/profile2";
    final private String key = "d2m4jqhr01qgtft74qp0d2m4jqhr01qgtft74qpg";

    final private String quoteQueryParam = "symbol";
    final private String tokenQueryParam = "token";

    public ProfileDTO getStockProfile(String symbol) {
        String url = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(host)
            .path(profileEndpoint)
            .queryParam(quoteQueryParam, symbol)
            .queryParam(tokenQueryParam, key)
            .build()
            .toString();
        ProfileDTO profileDTO = new RestTemplate().getForObject(url, ProfileDTO.class);
        return profileDTO;
    }

    public QuoteDTO getStockQuote(String symbol) {
        String url = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(host)
            .path(quoteEndpoint)
            .queryParam(quoteQueryParam, symbol)
            .queryParam(tokenQueryParam, key)
            .build()
            .toString();
        QuoteDTO quoteDTO = new RestTemplate().getForObject(url, QuoteDTO.class);
        return quoteDTO;
    }

  public List<StockInfoEntity> getSymbolList() {
    // System.out.println(this.stockInfoRepository.getSymbols());
    // return List.of("AAPL", "MMM", "AOS", "ABT");
    return this.stockInfoRepository.findAll();
  }

  public List<ProfileDTO> getStockProfiles() {
    List<ProfileDTO> profileDTOs = new ArrayList<>();
    for(StockInfoEntity symbol : this.getSymbolList()) {
      ProfileDTO profileDTO = this.getStockProfile(symbol.getSymbol());
      profileDTOs.add(profileDTO);
      System.out.println(profileDTO.getSymbol());
    }
    return profileDTOs;
  }

  public List<QuoteDTO> getStockQuotes() {
    List<QuoteDTO> quoteDtos = new ArrayList<>();
    for(StockInfoEntity symbol : this.getSymbolList()) {
      QuoteDTO quoteDto = this.getStockQuote(symbol.getSymbol());
      quoteDtos.add(quoteDto);
      System.out.println(quoteDto.getPrice());
    }
    return quoteDtos;
  }

  @Override
  public List<HeatMapDTO> generateData() {
    List<QuoteDTO> quoteDTOs = this.getStockQuotes();
    List<ProfileDTO> profileDTOs = this.getStockProfiles();
    List<HeatMapDTO> heatMapDTOs = new ArrayList<>();
    for (int i = 0; i < quoteDTOs.size(); i++) {
        heatMapDTOs.add(HeatMapDTO.builder()
          .industry(profileDTOs.get(i).getIndustry())
          .marketCapitalization(profileDTOs.get(i).getMarketCapitalization())
          .priceChangePercentage(quoteDTOs.get(i).getPercentChange())
          .symbol(profileDTOs.get(i).getSymbol())
          .build());
    }
    return heatMapDTOs;
  }
}
