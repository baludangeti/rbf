# Functional Test Plan

| ID | Module | Scenario | API/UI Surface | Database Validation | Expected Result |
|---|---|---|---|---|---|
| FT-ORG-001 | Organization | Create organization | `POST /api/organizations` | `rbf_org_db.organizations` | Organization row created with unique code/email |
| FT-ORG-002 | Organization | Duplicate organization code/email | `POST /api/organizations` | `rbf_org_db.organizations` count unchanged | `400` or validation error |
| FT-AUTH-001 | Auth | Valid login | `POST /api/auth/login` | `rbf_auth_db.users`, roles | JWT returned with tenant and permissions |
| FT-AUTH-002 | Auth | Invalid password | `POST /api/auth/login` | No mutation | `400` or `401` |
| FT-SEC-001 | Gateway | Missing token | `GET /api/products` | No mutation | `401` |
| FT-SEC-002 | Gateway | Invalid token | `GET /api/products` | No mutation | `401` |
| FT-RBAC-001 | RBAC | Create permission | `POST /api/auth/permissions` | `permissions` | Permission active |
| FT-RBAC-002 | RBAC | Create role and assign permission | `POST /api/auth/roles`, `POST /roles/{id}/permissions` | `roles`, `role_permissions` | Role has authority |
| FT-RBAC-003 | RBAC | Assign role to user | `POST /api/auth/users/{id}/roles` | `user_roles` | Login JWT includes role permissions |
| FT-PROD-001 | Product | Create category | `POST /api/product-categories` | `product_categories.org_id` | Category visible only to same org |
| FT-PROD-002 | Product | Create/update/search product | `POST/PUT/GET /api/products` | `products.org_id`, SKU, barcode | Product searchable by name/SKU/barcode |
| FT-PROD-003 | Product | Deactivate product | `DELETE /api/products/{id}` | `products.active=false` | Product no longer active |
| FT-INV-001 | Inventory | Add stock | `POST /api/inventory/add` | `inventory.quantity` | Quantity increases |
| FT-INV-002 | Inventory | Deduct stock | `POST /api/inventory/deduct` | `inventory.quantity` | Quantity decreases |
| FT-INV-003 | Inventory | Prevent negative stock | `POST /api/inventory/deduct` | No mutation | `400` |
| FT-TAX-001 | Tax | GST slab setup | `POST /tax/slabs` | `tax_slabs.org_id` | Slab active |
| FT-TAX-002 | Tax | India intra-state GST | `POST /tax/calculate` | No mutation | CGST + SGST breakup |
| FT-TAX-003 | Tax | India inter-state GST | `POST /tax/calculate` | No mutation | IGST breakup |
| FT-BILL-001 | Billing | Create invoice | `POST /api/billing/invoices` | `invoices`, `invoice_items` | Invoice totals calculated |
| FT-BILL-002 | Billing | Full POS flow | Gateway + service calls | invoice, payment, ledger, inventory, tax breakup | End-to-end bill posted |
| FT-PAY-001 | Payment | Cash/UPI/card payment | `POST /api/payments` | `payments.org_id` | Payment saved |
| FT-PAY-002 | Payment | Partial payment status | `GET /api/payments/invoice/{id}/status` | `payments` sum | Status partial |
| FT-PAY-003 | Payment | Credit sale settlement | Credit/payment APIs | `payments`, credit ledger | Outstanding amount reduced |
| FT-SR-001 | Sales Return | Search invoice and return one item | `GET /api/sales-returns/invoice/{invoiceId}`, `POST /api/sales-returns` | `sales_returns`, `sales_return_items` | Partial return saved |
| FT-SR-002 | Sales Return | Restore inventory and refund | Sales return service integrations | `inventory`, `payments`, `ledger_entries` | Stock restored, refund and reversal posted |
| FT-SR-003 | Sales Return | Return quantity greater than billed | `POST /api/sales-returns` | No mutation | `400` validation error |
| FT-PUR-001 | Purchase | Create supplier and purchase order | `POST /api/suppliers`, `POST /api/purchases` | `suppliers`, `purchases`, `purchase_items` | PO created for same org |
| FT-PUR-002 | Purchase | GRN increases inventory | `POST /api/purchases/{id}/grn` | `inventory`, purchase status | Stock increased and payable ledger created |
| FT-PUR-003 | Purchase | Purchase return | `POST /api/purchases/{id}/returns` | purchase return/payable/inventory records | Stock and payable reduced |
| FT-CUST-001 | Customer Credit | Create customer and credit account | `POST /api/customers`, `POST /api/customer-credits/accounts` | `customers`, `customer_credit_accounts` | Credit limit stored |
| FT-CUST-002 | Customer Credit | Credit sale and settlement | `POST /api/customer-credits/sales`, settlement API | `customer_credit_transactions` | Outstanding balance updated |
| FT-CUST-003 | Customer Credit | Prevent credit beyond limit | `POST /api/customer-credits/validate` | No mutation | Validation fails |
| FT-ACC-001 | Accounting | Sales ledger entry | `POST /api/accounting/ledger-entries` | `ledger_entries.org_id` | Ledger entry saved |
| FT-ACC-002 | Accounting | Tax payable and payment entries | Billing/payment integrations | `ledger_entries` | Debit/credit totals balanced |
| FT-ACC-003 | Accounting | Trial balance and P&L | Report/accounting APIs | Ledger tables | Financial statements return totals |
| FT-ACC-004 | Accounting | Customer and supplier ledger | Ledger APIs | Ledger tables filtered by party/org | Correct party balances |
| FT-REP-001 | Reports | Sales report | `GET /api/reports/sales` | Cross-service reads | Date-filtered sales returned |
| FT-REP-002 | Reports | Dashboard | `GET /api/reports/dashboard` | Cross-service reads | Metrics returned |
| FT-REP-003 | Reports | GST, inventory, payment, credit reports | `GET /api/reports/*` | Cross-service reads | Date-filtered summaries returned |
| FT-JSP-001 | Console | Login page | `GET /login` | No mutation | JSP `200`, CSS/JS load |
| FT-JSP-002 | Console | Register page | `GET /organization/register` | No mutation | JSP `200` |
| FT-JSP-003 | Console | Protected page without session | `GET /console/dashboard` | No mutation | Redirect to `/login` |
| FT-JSP-004 | Console | Product/inventory/POS/invoice/report pages | JSP routes + AJAX APIs | No unexpected mutation | Pages render, assets load, AJAX handles errors |
| FT-MT-001 | Multi-tenant | Org A product hidden from Org B | `GET /api/products/{id}` with Org B token | `products.org_id` | `404` or `403` |
| FT-MT-002 | Multi-tenant | Org A invoice hidden from Org B | `GET /api/billing/invoices/{id}` with Org B token | `invoices.org_id` | `404` or `403` |
| FT-MT-003 | Multi-tenant | Org A inventory/reports hidden from Org B | Inventory/report APIs with Org B token | Tenant-owned tables | `404`, `403`, or empty same-org result |
| FT-NEG-001 | Negative | Missing `X-ORG-ID` | Any tenant API | No mutation | `400` or `401` |
| FT-NEG-002 | Negative | Invalid product id | Billing invoice with bad item | No mutation | `404`/`400` |
| FT-NEG-003 | Negative | Backend service down | Stop product service, create invoice | No mutation | Gateway or billing returns service error |
| FT-NEG-004 | Negative | Duplicate invoice/payment/tax invalid data | Billing/payment/tax APIs | No mutation or rejected duplicate | Structured `400`/validation error |
| FT-NEG-005 | Negative | Database connection failure | Any service during DB outage | No partial mutation | Health `DOWN`, API returns service error |

## Role Hierarchy Expectations

`SUPER_ADMIN` includes all permissions. `ADMIN` includes manager/cashier-level permissions. `MANAGER` includes inventory/report permissions. `CASHIER` includes billing create and product view permissions.

## Database Checks

Run `sql/db-verification-queries.sql` after the Postman collection. The checks verify tenant ownership and side effects for invoice, inventory, payment, ledger, tax breakup, and reports data.
