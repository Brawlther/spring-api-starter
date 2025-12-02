CREATE VIEW cart_items_view AS
SELECT 
    id,
    BIN_TO_UUID(cart_id) as cart_id,
    product_id,
    quantity
FROM cart_items;