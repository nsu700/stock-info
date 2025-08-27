package home.kdkd.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.kdkd.stock.entity.StockInfoEntity;

public interface StockInfoRepository extends JpaRepository<StockInfoEntity, Long>{
  // @Query("SELECT symbol FROM stock_info")
  // List<String> getSymbols();
}
