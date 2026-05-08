# Production Implementation Steps 20-30

Scope: pure Spring Boot microservices only. No Eureka, Spring Cloud, Kafka, or third-party service platform. Each service runs independently, owns its database, communicates via REST, stores `org_id` in tenant tables, and filters every tenant query by `org_id`.

Common tenant rules for all steps:

- Read tenant from `X-ORG-ID`.
- Reject tenant APIs when `X-ORG-ID` is missing.
- Pass `orgId` from controller to service layer.
- Add `org_id` to every tenant-owned table.
- Use repository methods with `orgId`, such as `findByOrgIdAndId`.
- Forward `X-ORG-ID` in all `RestTemplate` calls.
- Do not trust client-provided `orgId` if a header is present.

Common response examples:

```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {},
  "timestamp": "2026-05-07T10:30:00"
}
```

Common error example:

```json
{
  "timestamp": "2026-05-07T10:30:00",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/example",
  "errors": {
    "field": "must not be blank"
  }
}
```

## 20. Purchase Module

Service: `purchase-service`

Purpose: manage supplier purchases, purchase orders, GRN, and inventory increase.

### Required Tables

```sql
CREATE TABLE purchases (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  supplier_name VARCHAR(150) NOT NULL,
  purchase_number VARCHAR(50) NOT NULL,
  status VARCHAR(30) NOT NULL,
  total DECIMAL(15,2) NOT NULL,
  grn_number VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  INDEX idx_purchases_org_status (org_id, status),
  UNIQUE KEY uk_purchases_org_number (org_id, purchase_number)
);

CREATE TABLE purchase_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  purchase_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  received_quantity INT NOT NULL DEFAULT 0,
  unit_cost DECIMAL(15,2) NOT NULL,
  line_total DECIMAL(15,2) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  INDEX idx_purchase_items_org_purchase (org_id, purchase_id),
  INDEX idx_purchase_items_org_product (org_id, product_id)
);
```

### Entity Classes

- `Purchase extends OrgScopedEntity`
- `PurchaseItem extends OrgScopedEntity`
- `PurchaseStatus`: `DRAFT`, `ORDERED`, `PARTIALLY_RECEIVED`, `RECEIVED`, `CANCELLED`

### DTOs

- `CreatePurchaseRequest`
- `CreatePurchaseItemRequest`
- `GrnRequest`
- `PurchaseResponse`
- `PurchaseItemResponse`

### Repository

- `PurchaseRepository`
  - `List<Purchase> findByOrgIdOrderByCreatedAtDesc(Long orgId)`
  - `Optional<Purchase> findByOrgIdAndId(Long orgId, Long id)`
  - `boolean existsByOrgIdAndPurchaseNumber(Long orgId, String purchaseNumber)`
- `PurchaseItemRepository`
  - `List<PurchaseItem> findByOrgIdAndPurchaseId(Long orgId, Long purchaseId)`

### Service

`PurchaseService`

Responsibilities:

- Create purchase order.
- Calculate line totals and purchase total.
- Receive goods through GRN.
- Increase inventory by calling Inventory Service.
- Mark purchase as `PARTIALLY_RECEIVED` or `RECEIVED`.

Integration:

- `POST inventory-service /api/inventory/add`
- Pass `X-ORG-ID`.

### Controller

Base path: `/api/purchases`

- `GET /api/purchases`
- `GET /api/purchases/{id}`
- `POST /api/purchases`
- `POST /api/purchases/{id}/grn`

### Validation

- `supplierId`: `@NotNull`
- `supplierName`: `@NotBlank`
- `purchaseNumber`: `@NotBlank`
- `items`: `@NotEmpty`
- `productId`: `@NotNull`
- `quantity`: `@Positive`
- `unitCost`: `@Positive`

### Sample Request

```json
{
  "supplierId": 301,
  "supplierName": "ABC Traders",
  "purchaseNumber": "PO-2026-001",
  "items": [
    {
      "productId": 1,
      "quantity": 50,
      "unitCost": 60.0
    }
  ]
}
```

### Sample Response

```json
{
  "id": 1,
  "purchaseNumber": "PO-2026-001",
  "supplierId": 301,
  "supplierName": "ABC Traders",
  "status": "ORDERED",
  "total": 3000.0,
  "orgId": 101,
  "items": [
    {
      "productId": 1,
      "quantity": 50,
      "receivedQuantity": 0,
      "unitCost": 60.0,
      "lineTotal": 3000.0
    }
  ]
}
```

## 21. Customer Credit System

Service: `customer-credit-service`

Purpose: support credit sales, due tracking, payment settlement, and credit limit validation.

### Required Tables

```sql
CREATE TABLE customer_credit_accounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  customer_name VARCHAR(150) NOT NULL,
  credit_limit DECIMAL(15,2) NOT NULL,
  outstanding_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME,
  updated_at DATETIME,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  UNIQUE KEY uk_credit_account_org_customer (org_id, customer_id),
  INDEX idx_credit_account_org_active (org_id, active)
);

CREATE TABLE customer_credit_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  invoice_id BIGINT,
  transaction_type VARCHAR(30) NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  balance_after DECIMAL(15,2) NOT NULL,
  reference VARCHAR(100),
  created_at DATETIME,
  updated_at DATETIME,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  INDEX idx_credit_tx_org_customer (org_id, customer_id),
  INDEX idx_credit_tx_org_invoice (org_id, invoice_id)
);
```

### Entity Classes

- `CustomerCreditAccount extends OrgScopedEntity`
- `CustomerCreditTransaction extends OrgScopedEntity`
- `CreditTransactionType`: `CREDIT_SALE`, `SETTLEMENT`, `ADJUSTMENT`

### DTOs

- `CreditAccountRequest`
- `CreditAccountResponse`
- `CreditValidationRequest`
- `CreditValidationResponse`
- `CreditSaleRequest`
- `CreditSettlementRequest`
- `CreditTransactionResponse`

### Repository

- `CustomerCreditAccountRepository`
  - `Optional<CustomerCreditAccount> findByOrgIdAndCustomerId(Long orgId, Long customerId)`
  - `List<CustomerCreditAccount> findByOrgIdOrderByCustomerNameAsc(Long orgId)`
- `CustomerCreditTransactionRepository`
  - `List<CustomerCreditTransaction> findByOrgIdAndCustomerIdOrderByCreatedAtDesc(Long orgId, Long customerId)`

### Service

`CustomerCreditService`

Responsibilities:

- Create/update credit account.
- Validate available credit before invoice finalization.
- Record credit sale after invoice creation.
- Settle outstanding amount.
- Maintain transaction ledger per customer.

Integration:

- Billing Service calls `/api/customer-credits/validate` before credit invoice.
- Billing Service calls `/api/customer-credits/sales` after finalized credit invoice.
- Payment settlement can create accounting entries where needed.

### Controller

Base path: `/api/customer-credits`

- `GET /api/customer-credits`
- `GET /api/customer-credits/customers/{customerId}`
- `POST /api/customer-credits/accounts`
- `POST /api/customer-credits/validate`
- `POST /api/customer-credits/sales`
- `POST /api/customer-credits/customers/{customerId}/settlements`
- `GET /api/customer-credits/customers/{customerId}/transactions`

### Validation

- `customerId`: `@NotNull`
- `customerName`: `@NotBlank`
- `creditLimit`: `@Positive`
- `amount`: `@Positive`
- Settlement cannot exceed outstanding amount.
- Credit sale cannot exceed available credit.

### Sample Request

```json
{
  "customerId": 501,
  "customerName": "Retail Customer",
  "creditLimit": 10000.0
}
```

### Sample Response

```json
{
  "customerId": 501,
  "customerName": "Retail Customer",
  "creditLimit": 10000.0,
  "outstandingAmount": 1500.0,
  "availableCredit": 8500.0,
  "orgId": 101
}
```

## 22. GST / Tax Engine

Service: `tax-service`

Purpose: calculate India GST and international tax regimes such as VAT, Sales Tax, Zero-rated, Exempt, and Reverse Charge.

### Required Tables

```sql
CREATE TABLE tax_countries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL UNIQUE,
  country_name VARCHAR(100) NOT NULL,
  currency_code VARCHAR(10) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tax_regions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(20) NOT NULL,
  region_name VARCHAR(100) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE KEY uk_tax_region_country_region (country_code, region_code)
);

CREATE TABLE tax_regimes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL,
  regime_name VARCHAR(100) NOT NULL,
  regime_type VARCHAR(30) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tax_slabs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(20),
  tax_regime_id BIGINT NOT NULL,
  tax_name VARCHAR(100) NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  tax_rate DECIMAL(8,4) NOT NULL,
  hsn_sac_code VARCHAR(30),
  product_category_id BIGINT,
  effective_from DATE NOT NULL,
  effective_to DATE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME,
  updated_at DATETIME,
  INDEX idx_tax_slabs_org_lookup (org_id, country_code, region_code, tax_type, active)
);

CREATE TABLE tax_rules (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  source_country VARCHAR(10) NOT NULL,
  source_region VARCHAR(20),
  destination_country VARCHAR(10) NOT NULL,
  destination_region VARCHAR(20),
  tax_regime_id BIGINT NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  transaction_type VARCHAR(30) NOT NULL,
  customer_type VARCHAR(30) NOT NULL,
  product_category_id BIGINT,
  hsn_sac_code VARCHAR(30),
  priority INT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  INDEX idx_tax_rules_org_match (org_id, source_country, destination_country, transaction_type, customer_type, active)
);

CREATE TABLE invoice_tax_breakup (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  invoice_id BIGINT NOT NULL,
  invoice_item_id BIGINT,
  tax_name VARCHAR(100) NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  tax_rate DECIMAL(8,4) NOT NULL,
  taxable_amount DECIMAL(15,2) NOT NULL,
  tax_amount DECIMAL(15,2) NOT NULL,
  country_code VARCHAR(10),
  region_code VARCHAR(20),
  created_at DATETIME,
  INDEX idx_invoice_tax_org_invoice (org_id, invoice_id)
);

CREATE TABLE customer_tax_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(20),
  tax_registration_number VARCHAR(50),
  customer_type VARCHAR(30) NOT NULL,
  tax_exempt BOOLEAN NOT NULL DEFAULT FALSE,
  reverse_charge_applicable BOOLEAN NOT NULL DEFAULT FALSE,
  UNIQUE KEY uk_customer_tax_org_customer (org_id, customer_id)
);
```

### Entity Classes

- `TaxCountry`
- `TaxRegion`
- `TaxRegime`
- `TaxSlab extends OrgScopedEntity`
- `TaxRule extends OrgScopedEntity`
- `InvoiceTaxBreakup extends OrgScopedEntity`
- `CustomerTaxProfile extends OrgScopedEntity`
- Enums: `TaxType`, `TransactionType`, `CustomerType`

### DTOs

- `InternationalTaxCalculationRequest`
- `InternationalTaxCalculationResponse`
- `TaxBreakupDto`
- `SaveInvoiceTaxBreakupRequest`

### Repository

- `TaxSlabRepository`
- `TaxRuleRepository`
- `InvoiceTaxBreakupRepository`
- `TaxCountryRepository`
- `TaxRegionRepository`
- `TaxRegimeRepository`
- `CustomerTaxProfileRepository`

### Service

`InternationalTaxCalculationService`

Logic:

- Taxable amount = `quantity * unitPrice - discountAmount`.
- If customer exempt, return `EXEMPT` with zero tax.
- If reverse charge, return `REVERSE_CHARGE` with zero collected tax.
- India domestic same state: apply `CGST + SGST`.
- India domestic different state: apply `IGST`.
- Export: apply `ZERO_RATED` or configured international rule.
- VAT countries: apply `VAT`.
- USA: apply `SALES_TAX` based on customer region.
- Save invoice breakup after billing calculation.

Integration:

- Billing Service calls `/tax/calculate` per item.
- Billing Service calls `/tax/invoice-breakups` after invoice persistence.
- Accounting Service records tax payable, export revenue, or reverse charge accounting entries.

### Controller

- `POST /tax/countries`
- `GET /tax/countries`
- `POST /tax/regions`
- `GET /tax/regions`
- `POST /tax/regimes`
- `GET /tax/regimes`
- `POST /tax/slabs`
- `GET /tax/slabs`
- `PUT /tax/slabs/{id}`
- `DELETE /tax/slabs/{id}`
- `POST /tax/rules`
- `GET /tax/rules`
- `PUT /tax/rules/{id}`
- `DELETE /tax/rules/{id}`
- `POST /tax/calculate`
- `POST /tax/invoice-breakups`
- `GET /invoices/{invoiceId}/tax-breakup`

### Validation

- `orgId`: mandatory.
- `countryCode`: mandatory.
- `taxRate`: cannot be negative.
- `effectiveFrom` must be before `effectiveTo`.
- One highest-priority matching tax rule must apply.
- Tax registration number validation by country when provided.

### Sample Request

```json
{
  "orgId": 101,
  "sellerCountry": "IN",
  "sellerRegion": "KA",
  "customerCountry": "IN",
  "customerRegion": "KA",
  "customerType": "B2C",
  "customerTaxExempt": false,
  "reverseChargeApplicable": false,
  "productId": 1,
  "productCategoryId": 10,
  "hsnSacCode": "1006",
  "quantity": 2,
  "unitPrice": 400.0,
  "discountAmount": 40.0,
  "currencyCode": "INR",
  "exchangeRate": 1.0,
  "transactionType": "DOMESTIC"
}
```

### Sample Response

```json
{
  "taxableAmount": 760.0,
  "totalTaxAmount": 38.0,
  "totalAmount": 798.0,
  "taxRegimeName": "India GST",
  "taxBreakups": [
    {
      "taxName": "CGST",
      "taxType": "CGST",
      "taxRate": 2.5,
      "taxableAmount": 760.0,
      "taxAmount": 19.0
    },
    {
      "taxName": "SGST",
      "taxType": "SGST",
      "taxRate": 2.5,
      "taxableAmount": 760.0,
      "taxAmount": 19.0
    }
  ]
}
```

## 23. PDF Invoice Generation

Service: `billing-service`

Purpose: generate downloadable invoice PDF with company, customer, tax, item, total, and QR placeholder sections.

### Required Tables

No new core table is required if PDF is generated on demand from:

- `invoices`
- `invoice_items`
- `invoice_tax_breakup`

Optional table for audit/cache:

```sql
CREATE TABLE invoice_documents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  invoice_id BIGINT NOT NULL,
  document_type VARCHAR(30) NOT NULL,
  file_name VARCHAR(150) NOT NULL,
  generated_at DATETIME NOT NULL,
  generated_by VARCHAR(100),
  INDEX idx_invoice_docs_org_invoice (org_id, invoice_id)
);
```

### Entity Classes

- Optional: `InvoiceDocument extends OrgScopedEntity`

### DTOs

- `InvoicePdfRequest` optional for regeneration settings.
- Existing `InvoiceResponse`, `InvoiceItemResponse`, and tax breakup DTOs supply the data.

### Repository

- Optional: `InvoiceDocumentRepository`
- Required existing repositories:
  - `InvoiceRepository`
  - `InvoiceItemRepository`

### Service

`InvoicePdfService`

Responsibilities:

- Load invoice by `orgId` and `invoiceId`.
- Load invoice items and tax breakup.
- Render company details, GST/tax identifiers, line items, tax summary, totals, and QR placeholder.
- Return `byte[]` PDF.

Integration:

- Billing reads tax breakup from Tax Service or local stored breakup.
- Accounting entries are referenced in invoice footer if needed.

### Controller

- `GET /api/billing/invoices/{id}/pdf`

Response:

- Content type: `application/pdf`
- Header: `Content-Disposition: attachment; filename="invoice-{id}.pdf"`

### Validation

- Invoice must exist for current `org_id`.
- Draft invoices may be blocked or watermarked as draft.
- PDF request must not expose another organization invoice.

### Sample Request

```http
GET /api/billing/invoices/1001/pdf
Authorization: Bearer <jwt>
X-ORG-ID: 101
```

### Sample Response

```http
HTTP/1.1 200 OK
Content-Type: application/pdf
Content-Disposition: attachment; filename="invoice-1001.pdf"
```

## 24. Dashboard & Analytics

Service: `report-service`

Purpose: provide sales, revenue, stock, product, and outstanding payment analytics by calling service APIs.

### Required Tables

Report Service can remain stateless and call other services. Optional cache table:

```sql
CREATE TABLE report_snapshots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  report_type VARCHAR(50) NOT NULL,
  snapshot_date DATE NOT NULL,
  payload_json JSON NOT NULL,
  created_at DATETIME,
  INDEX idx_report_snapshots_org_type_date (org_id, report_type, snapshot_date)
);
```

### Entity Classes

- Optional: `ReportSnapshot extends OrgScopedEntity`

### DTOs

- `DashboardAnalyticsResponse`
- `DailySalesMetric`
- `MonthlyRevenueMetric`
- `TopSellingProductMetric`
- `LowStockAlert`
- `OutstandingPaymentMetric`
- `SalesReportResponse`
- `InventoryReportResponse`
- `FinancialReportResponse`

### Repository

- Optional: `ReportSnapshotRepository`

### Service

`ReportService`

Responsibilities:

- Fetch invoices from Billing Service.
- Fetch stock from Inventory Service.
- Fetch ledger entries from Accounting Service.
- Fetch payment status from Payment Service.
- Compute daily sales, monthly revenue, top products, low stock, and outstanding payments.

Integration:

- Billing: `/api/billing/invoices`
- Inventory: `/api/inventory`
- Payment: `/api/payments/invoice/statuses`
- Accounting: `/api/accounting/ledger`

### Controller

- `GET /api/reports/sales`
- `GET /api/reports/inventory`
- `GET /api/reports/financial`
- `GET /api/reports/dashboard`
- `GET /api/reports/dashboard/daily-sales`
- `GET /api/reports/dashboard/monthly-revenue`
- `GET /api/reports/dashboard/top-selling-products`
- `GET /api/reports/dashboard/low-stock-alerts`
- `GET /api/reports/dashboard/outstanding-payments`

### Validation

- `limit`: positive, max 100.
- `threshold`: zero or positive.
- Date range, when added, must be valid and bounded.

### Sample Response

```json
{
  "dailySales": [
    {
      "date": "2026-05-07",
      "invoiceCount": 12,
      "salesAmount": 25000.0
    }
  ],
  "monthlyRevenue": [
    {
      "month": "2026-05",
      "revenue": 450000.0
    }
  ],
  "topSellingProducts": [
    {
      "productId": 1,
      "quantitySold": 120,
      "salesAmount": 9600.0
    }
  ],
  "lowStockAlerts": [
    {
      "productId": 1,
      "quantity": 5,
      "threshold": 10
    }
  ],
  "outstandingPayments": [
    {
      "invoiceId": 1001,
      "invoiceTotal": 798.0,
      "paidAmount": 500.0,
      "dueAmount": 298.0
    }
  ]
}
```

## 25. Inventory Alerts

Service: `inventory-service`

Purpose: create low-stock, out-of-stock, and optional expiry notifications.

### Required Tables

```sql
CREATE TABLE inventory_notifications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  alert_type VARCHAR(30) NOT NULL,
  status VARCHAR(30) NOT NULL,
  message VARCHAR(255) NOT NULL,
  quantity INT,
  threshold_quantity INT,
  expiry_date DATE,
  created_at DATETIME,
  updated_at DATETIME,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  INDEX idx_inventory_notifications_org_status (org_id, status),
  INDEX idx_inventory_notifications_org_product (org_id, product_id)
);
```

Existing `inventory` table must include:

- `org_id`
- `product_id`
- `quantity`
- `low_stock_threshold`
- `expiry_date`

### Entity Classes

- `Inventory extends OrgScopedEntity`
- `InventoryNotification extends OrgScopedEntity`
- `InventoryAlertType`: `LOW_STOCK`, `OUT_OF_STOCK`, `EXPIRY`
- `InventoryNotificationStatus`: `OPEN`, `ACKNOWLEDGED`, `RESOLVED`

### DTOs

- `InventoryAlertSummaryResponse`
- `InventoryNotificationResponse`
- `StockRequest`
- `StockResponse`

### Repository

- `InventoryRepository`
  - `Optional<Inventory> findByOrgIdAndProductId(Long orgId, Long productId)`
  - `List<Inventory> findByOrgIdAndQuantityLessThanEqual(Long orgId, Integer threshold)`
- `InventoryNotificationRepository`
  - `List<InventoryNotification> findByOrgIdAndStatus(Long orgId, Status status)`

### Service

`InventoryAlertService`

Responsibilities:

- Evaluate alerts after stock add/deduct.
- Create out-of-stock notification when quantity is zero.
- Create low-stock notification when quantity is below threshold.
- Create expiry notification when expiry date is within configured window.
- Acknowledge notification.

Integration:

- Billing deduct stock and triggers alert evaluation.
- Purchase GRN adds stock and can resolve low/out-of-stock alerts.
- Report Service reads low stock alerts.

### Controller

- `GET /api/inventory/alerts`
- `GET /api/inventory/notifications`
- `POST /api/inventory/notifications/{id}/acknowledge`

### Validation

- Threshold cannot be negative.
- Quantity cannot be negative after deduction.
- Notification id must belong to current `org_id`.

### Sample Response

```json
{
  "lowStockCount": 4,
  "outOfStockCount": 1,
  "expiryAlertCount": 2,
  "notifications": [
    {
      "id": 15,
      "productId": 1,
      "alertType": "LOW_STOCK",
      "status": "OPEN",
      "message": "Stock is below threshold",
      "quantity": 5,
      "thresholdQuantity": 10,
      "orgId": 101
    }
  ]
}
```

## 26. Database Optimization

Scope: all database-backed services.

Purpose: optimize production queries, tenant filtering, reporting, billing, and inventory operations.

### Required Indexes

Core tenant indexes:

```sql
CREATE INDEX idx_products_org_id ON products (org_id, id);
CREATE INDEX idx_products_org_sku ON products (org_id, sku);
CREATE INDEX idx_products_org_barcode ON products (org_id, barcode);

CREATE INDEX idx_inventory_org_product ON inventory (org_id, product_id);
CREATE INDEX idx_inventory_org_quantity ON inventory (org_id, quantity);

CREATE INDEX idx_invoices_org_status_created ON invoices (org_id, status, created_at);
CREATE INDEX idx_invoice_items_org_invoice ON invoice_items (org_id, invoice_id);
CREATE INDEX idx_invoice_items_org_product ON invoice_items (org_id, product_id);

CREATE INDEX idx_payments_org_invoice ON payments (org_id, invoice_id);
CREATE INDEX idx_payments_org_status ON payments (org_id, status);

CREATE INDEX idx_purchases_org_status ON purchases (org_id, status);
CREATE INDEX idx_sales_returns_org_invoice ON sales_returns (org_id, invoice_id);
CREATE INDEX idx_credit_accounts_org_customer ON customer_credit_accounts (org_id, customer_id);
CREATE INDEX idx_tax_rules_org_match ON tax_rules (org_id, source_country, destination_country, transaction_type, customer_type, active);
CREATE INDEX idx_tax_breakup_org_invoice ON invoice_tax_breakup (org_id, invoice_id);
```

### Entity Classes

Add JPA indexes with `@Table(indexes = ...)` on high-traffic entities:

- `Product`
- `Inventory`
- `Invoice`
- `InvoiceItem`
- `Payment`
- `Purchase`
- `SalesReturn`
- `CustomerCreditAccount`
- `TaxRule`
- `InvoiceTaxBreakup`

### DTOs

Add pagination DTOs where response shape should be stable:

- `PageResponse<T>`
- `PagedInvoiceResponse`
- `PagedInventoryResponse`

### Repository

Use `Pageable` for list endpoints:

- `Page<Invoice> findByOrgId(Long orgId, Pageable pageable)`
- `Page<Invoice> findByOrgIdAndStatus(Long orgId, InvoiceStatus status, Pageable pageable)`
- `Page<Inventory> findByOrgId(Long orgId, Pageable pageable)`

### Service

Responsibilities:

- Avoid loading all rows for reports.
- Use batch APIs to avoid N+1 REST calls.
- Add query limits to dashboards.
- Use DB indexes that start with `org_id`.

### Controller

Pagination examples:

- `GET /api/billing/invoices/page?page=0&size=25&status=FINALIZED`
- `GET /api/inventory/page?page=0&size=50`

### Validation

- `page >= 0`
- `size` max 100 or 200 depending on endpoint.
- Sort fields must be allowlisted.

### Sample Response

```json
{
  "content": [],
  "page": 0,
  "size": 25,
  "totalElements": 120,
  "totalPages": 5
}
```

## 27. API Documentation

Artifact: `docs/api`

Purpose: document endpoint, method, request JSON, response JSON, and errors for all services.

### Required Tables

No database tables required.

### Entity Classes

No entity classes required.

### DTOs

Document all request/response DTOs:

- Auth, RBAC
- Organization
- Product
- Inventory
- Billing
- Payment
- Purchase
- Sales Return
- Customer Credit
- Tax
- Report
- Order
- Accounting when available

### Repository

No repository required.

### Service

No runtime service required.

### Controller

No runtime controller required.

### Validation

Documentation must include:

- Required headers.
- Required permissions.
- Validation error format.
- Tenant rules.
- Sample `400`, `401`, `403`, `404`, and `500` errors.

### Sample Documentation Entry

```md
### Create Product

Endpoint: `/api/products`
Method: `POST`
Headers: `Authorization`, `X-ORG-ID`
Permission: `PRODUCT_CREATE`

Request:
{
  "sku": "SKU-001",
  "barcode": "8901001000011",
  "name": "Rice 1kg",
  "price": 80.0,
  "gst": 5.0
}

Response:
{
  "id": 1,
  "sku": "SKU-001",
  "orgId": 101
}
```

## 28. Postman Collection

Artifact: `docs/postman`

Purpose: provide importable Postman collection with JWT variables.

### Required Tables

No database tables required.

### Entity Classes

No entity classes required.

### DTOs

Use request body samples from service DTOs.

### Repository

No repository required.

### Service

No runtime service required.

### Controller

No runtime controller required.

### Validation

Collection must include:

- `baseUrl`
- `jwtToken`
- `orgId`
- `productId`
- `invoiceId`
- Login test script that saves JWT.
- Protected APIs with bearer auth.
- Tenant APIs with `X-ORG-ID`.

### Required Folders

- Auth Service
- Product Service
- Inventory Service
- Billing Service
- Payment Service

### Sample Login Test Script

```javascript
const json = pm.response.json();
const token = json.token || (json.data && json.data.token);
if (token) {
  pm.collectionVariables.set("jwtToken", token);
}
const orgId = json.orgId || json.org_id || (json.data && (json.data.orgId || json.data.org_id));
if (orgId) {
  pm.collectionVariables.set("orgId", String(orgId));
}
```

## 29. Dockerization

Artifacts:

- `Dockerfile` per runnable service.
- Root `docker-compose.yml`.
- `.dockerignore`.

Purpose: run all services independently with separate MySQL containers and shared Docker bridge network.

### Required Tables

Tables are created in service-specific MySQL databases through migration scripts or Hibernate validation/update in non-production.

Production databases:

- `rbf_auth`
- `rbf_organization`
- `rbf_product`
- `rbf_inventory`
- `rbf_payment`
- `rbf_billing`
- `rbf_purchase`
- `rbf_sales_return`
- `rbf_customer_credit`
- `rbf_tax`
- `rbf_order`

### Entity Classes

No Docker-specific entity classes.

### DTOs

No Docker-specific DTOs.

### Repository

No Docker-specific repository.

### Service

Each service Dockerfile:

- Uses Java 17.
- Builds module with Maven.
- Runs one Spring Boot jar.
- Exposes only its service port.

### Controller

No Docker-specific controller.

### Validation

- Verify Dockerfile exists for every runnable service.
- Verify each service has its own database container.
- Verify all containers join `rbf-network`.
- Verify REST URLs use service names, not `localhost`.
- Verify MySQL credentials are environment-specific in production.

### Compose Network Example

```yaml
networks:
  rbf-network:
    driver: bridge
```

### Sample Service Environment

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://product-mysql:3306/rbf_product?useSSL=false&allowPublicKeyRetrieval=true
  SPRING_DATASOURCE_USERNAME: rbf
  SPRING_DATASOURCE_PASSWORD: rbf_password
  SPRING_DATASOURCE_DRIVER_CLASS_NAME: com.mysql.cj.jdbc.Driver
```

## 30. Production Deployment Guide

Artifact: `docs/production-deployment-guide.md`

Purpose: define production environment variables, properties, security, deployment order, backup, and rollback.

### Required Tables

No deployment-specific tables. Production must deploy all module tables before services start with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

### Entity Classes

No deployment-specific entity classes.

### DTOs

No deployment-specific DTOs.

### Repository

No deployment-specific repository.

### Service

Production deployment responsibilities:

- Provision databases.
- Apply migration scripts.
- Deploy services in dependency order.
- Run gateway smoke tests.
- Monitor logs, metrics, errors, and database pools.

### Controller

No deployment-specific controller.

### Validation

Production checklist:

- `SPRING_PROFILES_ACTIVE=prod`.
- `ddl-auto=validate`.
- Strong JWT secret.
- Only API Gateway exposed publicly.
- All service and MySQL ports private.
- Per-service DB users.
- Backups enabled and restore-tested.
- Logs redact secrets.
- Tenant filtering verified with `X-ORG-ID`.

### Deployment Order

```text
1. Network, ingress, TLS, DNS
2. MySQL databases and users
3. Schema scripts and indexes
4. Auth Service
5. Organization Service
6. Product Service
7. Inventory Service
8. Payment Service
9. Tax Service
10. Customer Credit Service
11. Purchase Service
12. Sales Return Service
13. Billing Service
14. Report Service
15. API Gateway
```

### Backup Strategy

- Full backup daily.
- Binary log or incremental backup every 15 to 60 minutes.
- Restore related databases to the same point in time.
- Test restore monthly.
- Keep backups outside the production host or cluster.

### Smoke Test Request Sequence

```text
POST /auth/login
POST /products
POST /inventory/add
POST /billing/invoices
GET /payments/invoice/{invoiceId}/status
GET /reports/dashboard
```
