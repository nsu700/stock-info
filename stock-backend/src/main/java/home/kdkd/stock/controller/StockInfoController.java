package home.kdkd.stock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import home.kdkd.stock.dto.HeatMapDTO;


@Controller
public interface StockInfoController {
    @GetMapping("/heatmap")
    List<HeatMapDTO> generateData();
}
