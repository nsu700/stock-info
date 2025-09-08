package home.kdkd.stock.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.entity.StockEntity;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.entity.StockProfileEntity;
import home.kdkd.stock.repository.StockOHLCRepository;
import home.kdkd.stock.repository.StockProfileRepository;
import home.kdkd.stock.repository.StockRepository;
import home.kdkd.stock.service.HeatMapService;

@Service
public class HeatMapServiceImpl implements HeatMapService{
    @Autowired
    private StockProfileRepository stockProfileRepository;

    @Autowired
    private StockOHLCRepository stockOHLCRepository;

    @Autowired
    private StockRepository stockRepository;

    public Double calculatePriceChangePercentage(String symbol) {
        List<StockOHLCEntity> lastTwoDays = this.stockOHLCRepository.findTop2BySymbolOrderByTimestampDesc(symbol);
        if (lastTwoDays == null || lastTwoDays.size() < 2) {
            return 0.0; // Not enough data, so change is zero
        }
        BigDecimal todayClose = lastTwoDays.get(0).getClosePrice();
        BigDecimal yesterdayClose = lastTwoDays.get(1).getClosePrice();
        if (yesterdayClose.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0; // Avoid division by zero
        }
        
        BigDecimal priceChange = todayClose.subtract(yesterdayClose);
        BigDecimal percentageChange = priceChange.divide(yesterdayClose, 4, RoundingMode.HALF_UP)
                                                  .multiply(BigDecimal.valueOf(100));
        return percentageChange.doubleValue();
    }

    @Override
    public List<HeatMapDTO> generateData() {
        List<String> symbols = this.stockRepository.getSymbols();
        List<HeatMapDTO> heatMapDTOs = new ArrayList<>();
        List<String> prioritySymbols = this.stockProfileRepository.findTop50OrderByMarketCapitalization();
        for(String symbol: symbols) {
            if (prioritySymbols.contains(symbol)) {
                StockProfileEntity stockProfileEntity = this.stockProfileRepository.findBySymbol(symbol);
                StockEntity stockEntity = this.stockRepository.findBySymbol(symbol);
                heatMapDTOs.add(HeatMapDTO.builder()
                    .symbol(symbol)
                    .industry(stockEntity.getSector())
                    .marketCapitalization(stockProfileEntity.getMarketCapitalization())
                    .priceChangePercentage(0)
                    .build());
            } else {
                StockProfileEntity stockProfileEntity = this.stockProfileRepository.findBySymbol(symbol);
                StockEntity stockEntity = this.stockRepository.findBySymbol(symbol);
                heatMapDTOs.add(HeatMapDTO.builder()
                    .symbol(symbol)
                    .industry(stockEntity.getSector())
                    .marketCapitalization(stockProfileEntity.getMarketCapitalization())
                    .priceChangePercentage(this.calculatePriceChangePercentage(symbol))
                    .build());
            }
        }
        return heatMapDTOs;
    }
}
