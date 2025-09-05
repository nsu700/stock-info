package home.kdkd.stock.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.FinnhubProfileDTO;
import home.kdkd.stock.dto.FinnhubQuoteDTO;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.repository.StockInfoRepository;
import home.kdkd.stock.repository.StockOHLCRepository;
import home.kdkd.stock.service.FinnHubHelperService;
import home.kdkd.stock.service.StockService;


@Service
public class StockServiceImpl implements StockService{
    @Autowired
    private StockInfoRepository stockInfoRepository;

    @Autowired
    private StockOHLCRepository stockOHLCRepository;

    @Autowired
    private FinnHubHelperService  finnHubHelperService;

    public List<String> getSymbolList() {
        return List.of("AAPL", "NVDA", "MSFT", "TSLA", "AMZN", "LLY", "JPM", "WMT", "PLTR", "CRM", "IBM", "XOM", "ABT");
        // return this.stockInfoRepository.findAll();
    }

    @Override
    public List<FinnhubProfileDTO> getStockProfiles() {
        List<FinnhubProfileDTO> profileDTOs = new ArrayList<>();
        for(String symbol : this.getSymbolList()) {
          FinnhubProfileDTO profileDTO = this.finnHubHelperService.getStockProfile(symbol);
          System.out.println(profileDTO.getSymbol());
          profileDTOs.add(profileDTO);
        }
        return profileDTOs;
    }

    @Override
    public List<FinnhubQuoteDTO> getStockQuotes() {
        List<FinnhubQuoteDTO> quoteDtos = new ArrayList<>();
        for(String symbol : this.getSymbolList()) {
          FinnhubQuoteDTO quoteDto = this.finnHubHelperService.getStockQuote(symbol);
          System.out.println(quoteDto.getPrice());
          quoteDtos.add(quoteDto);
        }
        return quoteDtos;
    }

    @Override
    public List<StockOHLCEntity> getStockDetails(String symbol) {
      return this.stockOHLCRepository.findBySymbol(symbol);
    }
}
