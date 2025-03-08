package com.tirth.salecampion.repository;
import com.tirth.salecampion.model.PriceHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    @Modifying
    @Transactional
    @Query("INSERT INTO PriceHistory (productId, price, date) VALUES (:productId, :price, CURRENT_TIMESTAMP)")
    void savePriceHistory(@Param("productId") Long productId, @Param("price") BigDecimal price);

    List<PriceHistory> findByProductId(long productId);
}
