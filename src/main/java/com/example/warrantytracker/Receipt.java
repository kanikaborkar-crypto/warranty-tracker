package com.example.warrantytracker;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String category;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    private int warrantyMonths;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private String receiptImgUrl;
    // Default constructor (required by JPA)
    public Receipt() {}

    public Receipt(String productName, String category, LocalDate purchaseDate, int warrantyMonths) {
        this.productName = productName;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.warrantyMonths = warrantyMonths;
        this.expiryDate = calculateExpiryDate();
    }

    // Automatically calculate expiry date
    @PrePersist
    @PreUpdate
    public void updateExpiryDate() {
        if (this.purchaseDate != null) {
            this.expiryDate = this.purchaseDate.plusMonths(this.warrantyMonths);
        }
    }

    public LocalDate calculateExpiryDate() {
        return this.purchaseDate != null ? this.purchaseDate.plusMonths(this.warrantyMonths) : null;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        updateExpiryDate();
    }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
        updateExpiryDate();
    }

    public LocalDate getExpiryDate() { return expiryDate; }

    public String getReceiptImgUrl() { return receiptImgUrl; }
    public void setReceiptImgUrl(String receiptImgUrl) { this.receiptImgUrl = receiptImgUrl; }


    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Checks if the warranty expires within the next 30 days
    public boolean isExpiringSoon() {
        if (expiryDate == null) return false;
        LocalDate today = LocalDate.now();
        return !expiryDate.isBefore(today) && expiryDate.isBefore(today.plusDays(30));
    }

    // Checks if the warranty is already expired
    public boolean isExpired() {
        if (expiryDate == null) return false;
        return expiryDate.isBefore(LocalDate.now());
    }

    // Calculates remaining days from today until expiry date
    public long getDaysRemaining() {
        if (this.expiryDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate);
    }

    // Returns a status string based on days remaining
    public String getWarrantyStatus() {
        long days = getDaysRemaining();
        if (days < 0) {
            return "EXPIRED";
        } else if (days <= 30) {
            return "EXPIRING_SOON";
        } else {
            return "ACTIVE";
        }
    }


// Inside the Receipt class:


    public int getProgressPercentage() {
        if (purchaseDate == null || expiryDate == null) return 0;
        long totalDays = ChronoUnit.DAYS.between(purchaseDate, expiryDate);
        if (totalDays <= 0) return 100;
        long elapsed = ChronoUnit.DAYS.between(purchaseDate, LocalDate.now());
        if (elapsed < 0) return 0;
        if (elapsed >= totalDays) return 100;
        return (int) ((elapsed * 100) / totalDays);
    }
}