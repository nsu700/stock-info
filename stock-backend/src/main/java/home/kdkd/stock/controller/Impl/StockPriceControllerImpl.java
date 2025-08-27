package home.kdkd.stock.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import home.kdkd.stock.controller.StockPriceController;
import home.kdkd.stock.entity.StockPriceEntity;
import home.kdkd.stock.service.FinHubService;


@RestController
public class StockPriceControllerImpl implements StockPriceController{
    @Autowired
    private FinHubService stockPriceService;

    @Override
    public StockPriceEntity getStockPrice(String symbol) {
        return this.stockPriceService.getStockPrice(symbol);
    }
    
}
