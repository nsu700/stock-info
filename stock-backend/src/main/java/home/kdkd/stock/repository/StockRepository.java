package home.kdkd.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import home.kdkd.stock.entity.StockEntity;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
    StockEntity findBySymbol(String symbol);
    @Query("SELECT s.symbol FROM StockEntity s")
    List<String> getSymbols();
}
