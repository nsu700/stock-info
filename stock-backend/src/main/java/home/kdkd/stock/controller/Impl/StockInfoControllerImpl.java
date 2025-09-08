package home.kdkd.stock.controller.Impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import home.kdkd.stock.controller.StockInfoController;
import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.entity.StockProfileEntity;
import home.kdkd.stock.service.HeatMapService;
import home.kdkd.stock.service.StockService;
import home.kdkd.stock.service.YahooHelperService;


@RestController
public class StockInfoControllerImpl implements StockInfoController{
    @Autowired
    private HeatMapService heatMapService;

    @Autowired
    private YahooHelperService yahooHelperService;

    @Autowired
    private StockService stockService;

    @Override
    public List<HeatMapDTO> generateData() {
        return this.heatMapService.generateData();
    }

    @Override
    public void saveOHLC(String symbol, int days) {
        Instant now = Instant.now();
        long period2 = now.getEpochSecond();
        Instant ago = now.minus(days, ChronoUnit.DAYS);
        long period1 = ago.getEpochSecond();
        this.yahooHelperService.processAndSaveYahooData(symbol, period1, period2);
    }

    @Override
    public List<StockOHLCEntity> getOHLCData(String symbol) {
        return this.stockService.getStockDetails(symbol);
    }

    @Override
    public List<StockProfileEntity> updateStockProfiles() {
        return this.stockService.updateStockProfile();
    }

    @Override
    public List<String> getSymbols() {
        return this.stockService.getSymbolList();
    }

    @Override
    public StockOHLCEntity getLastDayOHLC(String stockSymbol) {
        return this.stockService.getStockLastDayOHLC(stockSymbol);
    }
}
