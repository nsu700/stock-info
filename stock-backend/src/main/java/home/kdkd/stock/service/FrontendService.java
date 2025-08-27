package home.kdkd.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import home.kdkd.stock.dto.HeatMapDTO;

@Service
public interface FrontendService {
  List<HeatMapDTO> generateFrontendData();
}
