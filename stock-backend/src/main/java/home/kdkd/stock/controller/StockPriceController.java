package home.kdkd.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import home.kdkd.stock.entity.StockPriceEntity;


@Controller
public interface StockPriceController {
    @GetMapping("/quote")
    StockPriceEntity getStockPrice(@RequestParam String param);
}
