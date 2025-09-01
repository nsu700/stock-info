package home.kdkd.stock.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;
import home.kdkd.stock.repository.StockInfoRepository;
import home.kdkd.stock.service.FinnHubHelperService;
import home.kdkd.stock.service.StockService;


@Service
@PropertySource("classpath:application.yaml")
public class StockServiceImpl implements StockService{
    @Autowired
    private StockInfoRepository stockInfoRepository;

    @Autowired
    private FinnHubHelperService  finnHubHelperService;

    public List<String> getSymbolList() {
        return List.of("AAPL", "NVDA", "MSFT", "TSLA", "AMZN", "LLY", "JPM", "WMT", "PLTR", "CRM", "IBM", "XOM", "ABT");
        // return this.stockInfoRepository.findAll();
    }

    @Override
    public List<ProfileDTO> getStockProfiles() {
        List<ProfileDTO> profileDTOs = new ArrayList<>();
        for(String symbol : this.getSymbolList()) {
          ProfileDTO profileDTO = this.finnHubHelperService.getStockProfile(symbol);
          System.out.println(profileDTO.getSymbol());
          profileDTOs.add(profileDTO);
        }
        return profileDTOs;
    }

    @Override
    public List<QuoteDTO> getStockQuotes() {
        List<QuoteDTO> quoteDtos = new ArrayList<>();
        for(String symbol : this.getSymbolList()) {
          QuoteDTO quoteDto = this.finnHubHelperService.getStockQuote(symbol);
          System.out.println(quoteDto.getPrice());
          quoteDtos.add(quoteDto);
        }
        return quoteDtos;
    }


}
