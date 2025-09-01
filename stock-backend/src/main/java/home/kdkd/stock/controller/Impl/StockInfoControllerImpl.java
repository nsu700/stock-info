package home.kdkd.stock.controller.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import home.kdkd.stock.controller.StockInfoController;
import home.kdkd.stock.dto.HeatMapDTO;
import home.kdkd.stock.service.HeatMapService;


@RestController
public class StockInfoControllerImpl implements StockInfoController{
    @Autowired
    private HeatMapService heatMapService;

    @Override
    public List<HeatMapDTO> generateData() {
        return this.heatMapService.generateData();
    }
}
