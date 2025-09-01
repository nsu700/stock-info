package home.kdkd.stock.service;

import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;

public interface FinnHubHelperService {
    ProfileDTO getStockProfile(String symbol);
    QuoteDTO getStockQuote(String symbol);
}
