package home.kdkd.stock.service;

import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;

@Service
public interface FinHubService {
    // StockPriceEntity saveStockPrice();
    ProfileDTO getStockProfile(String symbol);
    QuoteDTO getStockQuote(String symbol);
}
