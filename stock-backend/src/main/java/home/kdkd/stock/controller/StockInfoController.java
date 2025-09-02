package home.kdkd.stock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import home.kdkd.stock.dto.HeatMapDTO;


@Controller
public interface StockInfoController {
    @GetMapping("/api/stocks/heatmap-data")
    List<HeatMapDTO> generateData();

    @GetMapping("/api/stocks/{symbol}/history/{days}")
    void saveOHLC(@PathVariable("symbol") String stockSymbol, @PathVariable("days") int numberOfDays);
}
