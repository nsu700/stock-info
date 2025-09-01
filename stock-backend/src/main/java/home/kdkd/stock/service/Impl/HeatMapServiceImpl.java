package home.kdkd.stock.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.dto.ProfileDTO;
import home.kdkd.stock.dto.QuoteDTO;
import home.kdkd.stock.service.HeatMapService;
import home.kdkd.stock.service.StockService;

@Service
public class HeatMapServiceImpl implements HeatMapService{

    @Autowired
    private StockService stockService;

    @Override
    public List<HeatMapDTO> generateData() {
        List<QuoteDTO> quoteDTOs = this.stockService.getStockQuotes();
        List<ProfileDTO> profileDTOs = this.stockService.getStockProfiles();
        List<HeatMapDTO> heatMapDTOs = new ArrayList<>();
        for (int i = 0; i < quoteDTOs.size(); i++) {
            heatMapDTOs.add(HeatMapDTO.builder()
              .industry(profileDTOs.get(i).getIndustry())
              .marketCapitalization(profileDTOs.get(i).getMarketCapitalization())
              .priceChangePercentage(quoteDTOs.get(i).getPercentChange())
              .symbol(profileDTOs.get(i).getSymbol())
              .build());
        }
        return heatMapDTOs;
    }
}
