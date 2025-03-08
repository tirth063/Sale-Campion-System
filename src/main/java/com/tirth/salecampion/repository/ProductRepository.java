package com.tirth.salecampion.repository;

import com.tirth.salecampion.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.currentPrice IS NOT NULL")
    Page<Product> findAllWithPrice(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.currentPrice = :newPrice, p.discount = :discount WHERE p.id = :productId")
    void updateProductPrice(@Param("productId") Long productId,
                            @Param("newPrice") BigDecimal newPrice,
                            @Param("discount") Integer discount);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.currentPrice = p.mrp, p.discount = 0 WHERE p.id IN :productIds")
    void resetPricesForProducts(@Param("productIds") List<Long> productIds);

}
