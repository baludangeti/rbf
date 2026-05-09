-- Run these checks after the functional Postman collection.

SELECT 'ORG_A_PRODUCTS' AS check_name, COUNT(*) AS count_value
FROM rbf_product_db.products
WHERE org_id = 101;

SELECT 'ORG_B_PRODUCTS' AS check_name, COUNT(*) AS count_value
FROM rbf_product_db.products
WHERE org_id = 202;

SELECT 'ORG_A_INVOICES' AS check_name, COUNT(*) AS count_value
FROM rbf_billing_db.invoices
WHERE org_id = 101;

SELECT 'ORG_A_INVOICE_ITEMS' AS check_name, COUNT(*) AS count_value
FROM rbf_billing_db.invoice_items
WHERE org_id = 101;

SELECT 'ORG_A_TAX_BREAKUP' AS check_name, COUNT(*) AS count_value
FROM rbf_tax_db.invoice_tax_breakup
WHERE org_id = 101;

SELECT 'ORG_A_PAYMENTS' AS check_name, COUNT(*) AS count_value
FROM rbf_payment_db.payments
WHERE org_id = 101;

SELECT 'ORG_A_LEDGER' AS check_name, COUNT(*) AS count_value
FROM rbf_accounting_db.ledger_entries
WHERE org_id = 101;

SELECT 'CROSS_ORG_PRODUCT_LEAK' AS check_name, COUNT(*) AS count_value
FROM rbf_product_db.products
WHERE org_id = 202 AND sku LIKE 'FUNC-A-%';

SELECT 'NEGATIVE_INVENTORY' AS check_name, COUNT(*) AS count_value
FROM rbf_inventory_db.inventory
WHERE quantity < 0;

SELECT 'INVENTORY_AFTER_BILLING' AS check_name, product_id, org_id, quantity
FROM rbf_inventory_db.inventory
WHERE org_id IN (101, 202)
ORDER BY org_id, product_id;
