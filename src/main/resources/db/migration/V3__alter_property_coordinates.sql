-- Alter longitude and latitude column types to prevent numeric field overflow for global coordinates
ALTER TABLE properties ALTER COLUMN longitude TYPE NUMERIC(11, 8);
ALTER TABLE properties ALTER COLUMN latitude TYPE NUMERIC(10, 8);
