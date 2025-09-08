package home.kdkd.stock.service;

import home.kdkd.stock.dto.FinnhubQuoteDTO;
import home.kdkd.stock.entity.StockProfileEntity;

public interface FinnHubHelperService {
    StockProfileEntity getStockProfile(String symbol);
    FinnhubQuoteDTO getStockQuote(String symbol);
}
