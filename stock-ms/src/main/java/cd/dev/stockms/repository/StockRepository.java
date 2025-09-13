package cd.dev.stockms.repository;

import cd.dev.stockms.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProductId(Long productId);;
}