ALTER TABLE book 
ADD COLUMN rate DOUBLE NOT NULL DEFAULT 0.0 AFTER price;
