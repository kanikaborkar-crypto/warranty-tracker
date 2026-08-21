package com.example.warrantytracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT r FROM Receipt r WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "UPPER(r.productName) LIKE UPPER(CONCAT('%', :keyword, '%')) OR " +
            "UPPER(r.category) LIKE UPPER(CONCAT('%', :keyword, '%'))) AND " +
            "(:category IS NULL OR :category = '' OR UPPER(r.category) = UPPER(:category))")
    List<Receipt> searchAndFilter(@Param("keyword") String keyword, @Param("category") String category);

    // Matches 'private LocalDate expiryDate;' in Receipt.java
    List<Receipt> findByExpiryDate(LocalDate expiryDate);
}