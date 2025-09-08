package home.kdkd.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "stock")
public class StockEntity {
    private String name;
    private String symbol;
    private String sector;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
