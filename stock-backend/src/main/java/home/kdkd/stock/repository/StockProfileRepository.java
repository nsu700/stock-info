package home.kdkd.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import home.kdkd.stock.entity.StockProfileEntity;

public interface StockProfileRepository extends JpaRepository<StockProfileEntity, Long>{
  StockProfileEntity findBySymbol(String symbol);
  @Query("SELECT s.symbol FROM StockProfileEntity s ORDER BY s.marketCapitalization DESC LIMIT 50")
  List<String> findTop50OrderByMarketCapitalization();
}
