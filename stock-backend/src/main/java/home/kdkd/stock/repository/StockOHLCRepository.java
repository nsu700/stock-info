package home.kdkd.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import home.kdkd.stock.entity.StockOHLCEntity;

public interface StockOHLCRepository extends JpaRepository<StockOHLCEntity, Long>{
    List<StockOHLCEntity> findBySymbol(String symbol);
    StockOHLCEntity findTopBySymbolOrderByTimestampDesc(String symbol);
    List<StockOHLCEntity> findTop2BySymbolOrderByTimestampDesc(String symbol);
}
