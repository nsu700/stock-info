package home.kdkd.stock.service;

import java.util.List;

import home.kdkd.stock.dto.FinnhubQuoteDTO;
import home.kdkd.stock.entity.StockOHLCEntity;
import home.kdkd.stock.entity.StockProfileEntity;

public interface StockService {
    List<String> getSymbolList();
    List<FinnhubQuoteDTO> getStockQuotes();
    List<StockOHLCEntity> getStockDetails(String symbol);
    List<StockProfileEntity> updateStockProfile();
    StockOHLCEntity getStockLastDayOHLC(String symbol);
}
