package home.kdkd.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.kdkd.stock.entity.StockOHLCEntity;

public interface StockOHLCRepository extends JpaRepository<StockOHLCEntity, Long>{

}
