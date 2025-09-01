package home.kdkd.stock.service;

import java.util.List;

import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;

public interface StockService {
    List<ProfileDTO> getStockProfiles();
    List<QuoteDTO> getStockQuotes();
}
