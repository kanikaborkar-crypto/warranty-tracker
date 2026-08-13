package com.example.warrantytracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;

    // 1. Dashboard Page
    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "category", required = false) String category,
                               Model model) {
        List<Receipt> listReceipts;

        // Clean up inputs
        String cleanKeyword = (keyword != null) ? keyword.trim() : "";
        String cleanCategory = (category != null && !category.equalsIgnoreCase("all")) ? category.trim() : "";

        if (!cleanKeyword.isEmpty() || !cleanCategory.isEmpty()) {
            listReceipts = receiptRepository.searchAndFilter(cleanKeyword, cleanCategory);
        } else {
            listReceipts = receiptRepository.findAll();
        }

        long activeCount = listReceipts.stream().filter(r -> !r.isExpired() && !r.isExpiringSoon()).count();
        long expiringSoonCount = listReceipts.stream().filter(Receipt::isExpiringSoon).count();
        long expiredCount = listReceipts.stream().filter(Receipt::isExpired).count();

        model.addAttribute("listReceipts", listReceipts);
        model.addAttribute("totalReceipts", listReceipts.size());
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("expiringSoonCount", expiringSoonCount);
        model.addAttribute("expiredCount", expiredCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        return "dashboard";
    }

    // 2. Add Receipt Form
    @GetMapping("/showNewReceiptForm")
    public String showNewReceiptForm(Model model) {
        Receipt receipt = new Receipt();
        model.addAttribute("receipt", receipt);
        return "add-receipt";
    }

    // 3. Save Receipt
    @PostMapping("/saveReceipt")
    public String saveReceipt(@ModelAttribute("receipt") Receipt receipt,
                              @RequestParam("file") MultipartFile file) {
        handleFileUpload(receipt, file);
        receiptRepository.save(receipt);
        return "redirect:/";
    }

    // 4. Update Receipt Form
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid receipt Id:" + id));
        model.addAttribute("receipt", receipt);
        return "update-receipt";
    }

    // 5. Delete Receipt
    @GetMapping("/deleteReceipt/{id}")
    public String deleteReceipt(@PathVariable(value = "id") Long id) {
        receiptRepository.deleteById(id);
        return "redirect:/";
    }

    // 6. Controller Endpoint to Serve Uploaded Files Directly
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").toAbsolutePath().normalize().resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to save file locally
    private void handleFileUpload(Receipt receipt, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                receipt.setReceiptImgUrl("/uploads/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}