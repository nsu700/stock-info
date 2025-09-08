package home.kdkd.stock.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.FinnhubQuoteDTO;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.entity.StockProfileEntity;
import home.kdkd.stock.repository.StockOHLCRepository;
import home.kdkd.stock.repository.StockProfileRepository;
import home.kdkd.stock.repository.StockRepository;
import home.kdkd.stock.service.FinnHubHelperService;
import home.kdkd.stock.service.StockService;


@Service
public class StockServiceImpl implements StockService{
    // 1. Declare all dependencies as private final fields
    private final StockProfileRepository stockProfileRepository;
    private final StockOHLCRepository stockOHLCRepository;
    private final FinnHubHelperService finnHubHelperService;
    private final StockRepository stockRepository;

    // 2. Create a single constructor to inject all dependencies
    @Autowired
    public StockServiceImpl(
            StockProfileRepository stockProfileRepository,
            StockOHLCRepository stockOHLCRepository,
            FinnHubHelperService finnHubHelperService,
            StockRepository stockRepository) {
        this.stockProfileRepository = stockProfileRepository;
        this.stockOHLCRepository = stockOHLCRepository;
        this.finnHubHelperService = finnHubHelperService;
        this.stockRepository = stockRepository;
    }

    @Override
    public List<String> getSymbolList() {
        return this.stockRepository.getSymbols();
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

    @Override
    public List<StockProfileEntity> updateStockProfile() {
        List<StockProfileEntity> stockProfileEntitys = new ArrayList<>();
        int number = 0;
        for(String symbol : this.getSymbolList()) {
          System.out.println("Progressing the stock: " + symbol + ", number " + number);
          StockProfileEntity stockProfileEntity = this.finnHubHelperService.getStockProfile(symbol);
          System.out.println(stockProfileEntity);
          stockProfileEntitys.add(stockProfileEntity);
          try {
            Thread.sleep(1100);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          number++;
        }

        return this.stockProfileRepository.saveAll(stockProfileEntitys);
    }

    @Override
    public StockOHLCEntity getStockLastDayOHLC(String symbol) {
      return this.stockOHLCRepository.findTopBySymbolOrderByTimestampDesc(symbol);
    }
}
