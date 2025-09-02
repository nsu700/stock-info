package home.kdkd.stock.service;

import java.util.List;

import home.kdkd.stock.dto.FinnhubProfileDTO;
import home.kdkd.stock.dto.FinnhubQuoteDTO;

public interface StockService {
    List<FinnhubProfileDTO> getStockProfiles();
    List<FinnhubQuoteDTO> getStockQuotes();
}
