CREATE VIEW carts_view AS
SELECT 
    BIN_TO_UUID(id) as uuid,
    date_created
FROM carts;