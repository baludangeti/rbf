# End-to-End Retail Billing Test

## Prerequisites

- MySQL is running on `localhost:3306`.
- Databases are auto-created by JDBC URLs:
  - `rbf_auth_db`
  - `rbf_org_db`
  - `rbf_product_db`
  - `rbf_inventory_db`
  - `rbf_billing_db`
  - `rbf_payment_db`
  - `rbf_accounting_db`
  - `rbf_report_db`
  - `rbf_tax_db`
- Start services with `start-all.bat` or `./start-all.sh`.
- Confirm all services with `health-check.bat` or `./health-check.sh`.

## Flow

1. Open `http://localhost:8090/organization/register`.
2. Register an organization and first admin user.
3. Login at `http://localhost:8090/login`.
4. Confirm the session stores:
   - `JWT_TOKEN`
   - `ORG_ID`
   - `USER_ID`
   - `USERNAME`
   - `ROLES`
   - `PERMISSIONS`
5. Create a product from `http://localhost:8090/product/products`.
6. Add inventory stock from `http://localhost:8090/inventory/stock`.
7. Create a GST/tax slab from `http://localhost:8090/console/tax-settings`.
8. Open POS at `http://localhost:8090/billing/pos`.
9. Search product by name, SKU, or barcode.
10. Add item to cart, confirm GST breakup, select payment mode, and create invoice.
11. Verify billing service calls:
    - product service for item details
    - inventory service to deduct stock
    - payment service to save payment
    - accounting service to create ledger entry
    - tax service for GST calculation
12. Open `http://localhost:8090/reports/sales-report` and confirm the invoice appears.
13. Open invoice print view from the console and verify GST invoice details.

## API Smoke Sequence

Use the gateway at `http://localhost:8080`.

```http
POST /api/auth/login
GET /api/products
POST /api/inventory/add
POST /api/tax/calculate
POST /api/billing/invoices
GET /api/accounting/ledger-entries
GET /api/reports/dashboard
```

Every authenticated request must include:

```http
Authorization: Bearer <JWT_TOKEN>
X-ORG-ID: <ORG_ID>
```
