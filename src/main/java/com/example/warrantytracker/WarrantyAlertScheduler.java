package com.example.warrantytracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class WarrantyAlertScheduler {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.alert-recipient}")
    private String recipientEmail;

    // Test interval: runs every 10 seconds
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendUpcomingExpiryAlerts() {
        LocalDate targetDate = LocalDate.now().plusDays(7);
        List<Receipt> expiringSoon = receiptRepository.findByExpiryDate(targetDate);

        if (!expiringSoon.isEmpty()) {
            StringBuilder body = new StringBuilder("⚠️ Upcoming Warranty Expirations (in 7 days):\n\n");
            for (Receipt item : expiringSoon) {
                body.append("• ")
                        .append(item.getProductName() != null ? item.getProductName() : "Item")
                        .append(" (Expires: ").append(item.getExpiryDate()).append(")\n");
            }

            emailService.sendAlert(
                    recipientEmail,
                    "⚠️ Action Required: " + expiringSoon.size() + " Warranty Expiring Soon!",
                    body.toString()
            );
        }
    }
}