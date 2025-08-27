package home.kdkd.stock.service;

import java.util.List;

import home.kdkd.stock.dto.HeatMapDTO;

public interface StockService {
  List<HeatMapDTO> generateData();
}
