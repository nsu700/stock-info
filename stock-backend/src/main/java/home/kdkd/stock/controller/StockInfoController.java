package home.kdkd.stock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.entity.StockOHLCEntity;


@Controller
public interface StockInfoController {
    @GetMapping("/api/stocks/heatmap-data")
    List<HeatMapDTO> generateData();

    @GetMapping("/api/stocks/{symbol}/history/{days}")
    void saveOHLC(@PathVariable("symbol") String stockSymbol, @PathVariable("days") int numberOfDays);

    @GetMapping("/api/stocks/{symbol}/price-history")
    List<StockOHLCEntity> getOHLCData(@PathVariable("symbol") String stockSymbol);
}
