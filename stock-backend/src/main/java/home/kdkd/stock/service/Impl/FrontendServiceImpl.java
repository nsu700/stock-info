package home.kdkd.stock.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;
import home.kdkd.stock.service.FinHubService;
import home.kdkd.stock.service.FrontendService;

public class FrontendServiceImpl implements FrontendService{
  @Autowired
  private FinHubService finHubService;

  public HeatMapDTO getHeatMapDTO(String symbolString) {
    ProfileDTO profileDTO = this.finHubService.getStockProfile(symbolString);
    QuoteDTO quoteDTO = this.finHubService.getStockQuote(symbolString);
    HeatMapDTO heatMapDTO = HeatMapDTO.builder()
      .industry(profileDTO.getIndustry())
      .marketCapitalization(profileDTO.getMarketCapitalization())
      .priceChangePercentage(quoteDTO.getPercentChange())
      .symbol(symbolString)
      .build();
    return heatMapDTO;
  }

  public List<HeatMapDTO> generateFrontendData() {

  }
}
