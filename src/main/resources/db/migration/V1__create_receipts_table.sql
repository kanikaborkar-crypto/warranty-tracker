CREATE TABLE receipts (
                          id BIGSERIAL PRIMARY KEY,
                          category VARCHAR(255),
                          product_name VARCHAR(255) NOT NULL,
                          purchase_date DATE NOT NULL,
                          warranty_months INT NOT NULL,
                          expiry_date DATE NOT NULL,
                          receipt_img_url VARCHAR(255)
);