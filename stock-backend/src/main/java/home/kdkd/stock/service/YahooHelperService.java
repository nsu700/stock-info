package home.kdkd.stock.service;

public interface YahooHelperService {
    void processAndSaveYahooData(String symbol, long period1, long period2);
}
