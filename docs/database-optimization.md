# Database Optimization Notes

## Indexes Added

- `invoices(org_id, created_at)`: supports newest-first invoice lists and dashboard daily/monthly sales scans.
- `invoices(org_id, status, updated_at)`: supports draft/held invoice queues.
- `invoices(org_id, hold_reference)`: supports resume billing by hold reference.
- `invoice_items(org_id, invoice_id)`: supports invoice detail and tax/PDF joins.
- `invoice_items(org_id, product_id)`: supports top-selling product aggregation.
- `payments(org_id, invoice_id, created_at)`: supports payment status and outstanding-payment checks.
- `payments(org_id, status)`: supports payment status dashboards.
- `inventory(org_id, product_id)`: kept as unique lookup for stock mutation.
- `inventory(org_id, quantity)`: supports low/out-of-stock scans.
- `inventory(org_id, expiry_date)`: supports expiry alerts.
- `inventory_notifications(org_id, status, created_at)`: supports open notification feeds.
- `inventory_notifications(org_id, product_id, alert_type, status)`: prevents duplicate open alerts.
- `products(org_id, sku)`, `products(org_id, barcode)`, `products(org_id, name)`: supports retail search and barcode/SKU billing.
- `tax_rules(org_id, active, priority)` and route columns: supports highest-priority tax rule selection.
- `tax_slabs(org_id, tax_regime_id, tax_type, active)`: supports tax slab lookup during billing.
- `invoice_tax_breakup(org_id, invoice_id)`: supports PDF invoice and invoice tax audit screens.
- `customer_credit_accounts(org_id, customer_id)` and `customer_credit_accounts(org_id, due_amount)`: supports credit-limit validation and outstanding credit reporting.

## Query Optimizations

- Invoice reads now have a paginated endpoint: `GET /api/billing/invoices/page?page=0&size=25`.
- Inventory reads now have a paginated endpoint: `GET /api/inventory/page?page=0&size=50`.
- Report Service no longer performs one payment-status HTTP call per invoice for outstanding payments. It now calls Payment Service with a batch request.
- Report Service forwards the bearer token and `X-ORG-ID` to downstream services so downstream services keep using tenant-filtered indexed queries.

## Design Decisions

- Indexes start with `org_id` because every tenant-scoped query filters by organization first.
- Sort columns such as `created_at` and `updated_at` are included in list indexes to avoid extra sorting work.
- Product lookup indexes match cashier workflows: barcode scan, SKU lookup, and name search.
- Payment reporting is batched at the service boundary because cross-service N+1 calls become expensive before database queries do.
- Existing unpaginated endpoints were preserved for compatibility, while new paginated endpoints were added for production use.
