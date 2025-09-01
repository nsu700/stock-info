package home.kdkd.stock.service;

import java.util.List;

import home.kdkd.stock.dto.HeatMapDTO;

public interface HeatMapService {
    List<HeatMapDTO> generateData();
}
