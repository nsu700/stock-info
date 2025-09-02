package home.kdkd.stock.service;

import home.kdkd.stock.dto.FinnhubProfileDTO;
import home.kdkd.stock.dto.FinnhubQuoteDTO;

public interface FinnHubHelperService {
    FinnhubProfileDTO getStockProfile(String symbol);
    FinnhubQuoteDTO getStockQuote(String symbol);
}
