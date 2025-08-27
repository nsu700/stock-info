package home.kdkd.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.kdkd.stock.entity.StockPriceEntity;

public interface StockPriceRepository extends JpaRepository<StockPriceEntity, Long>{

}
