# API Documentation - All Services

Base tenant headers:

```http
Authorization: Bearer <jwt-token>
X-ORG-ID: 101
Content-Type: application/json
```

Standard error response:

```json
{
  "timestamp": "2026-05-07T10:30:00",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/example",
  "errors": {
    "field": "reason"
  }
}
```

## API Gateway

Gateway base URL: `http://localhost:9101`

The gateway is the single entry point. It proxies matching paths to independent Spring Boot services using `RestTemplate` and forwards headers, especially `Authorization` and `X-ORG-ID`.

| Endpoint | Method | Downstream service |
| --- | --- | --- |
| `/auth/**` | ANY | Auth Service |
| `/products/**` | ANY | Product Service |
| `/billing/**` | ANY | Billing Service |
| `/inventory/**` | ANY | Inventory Service |
| `/purchases/**` | ANY | Purchase Service |
| `/sales-returns/**` | ANY | Sales Return Service |
| `/customer-credits/**` | ANY | Customer Credit Service |
| `/tax/**` | ANY | Tax Service |
| `/reports/**` | ANY | Report Service |
| `/accounting/**` | ANY | Accounting Service |

Example request:

```http
GET /products/1
Authorization: Bearer <jwt-token>
X-ORG-ID: 101
```

Example response:

```json
{
  "id": 1,
  "sku": "SKU-001",
  "barcode": "8901001000011",
  "name": "Rice 1kg",
  "price": 80.0,
  "gst": 5.0,
  "orgId": 101
}
```

Error responses: `401`, `403`, downstream `4xx/5xx` responses are passed back.

## Auth Service

Base path: `/api/auth`, gateway path: `/auth`

### Login

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/auth/login` | POST | Validate credentials and return JWT with `org_id`, roles, and permissions. |

Request JSON:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response JSON:

```json
{
  "token": "<jwt-token>",
  "userId": 1,
  "username": "admin",
  "orgId": 101,
  "roles": ["ADMIN"],
  "permissions": ["PRODUCT_CREATE", "BILLING_CREATE", "REPORT_VIEW"]
}
```

Error responses: `400` invalid request, `401` invalid credentials, `500` unexpected login error.

## Auth RBAC APIs

Base paths: `/api/roles`, `/api/permissions`, `/api/users`

### Permissions

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/permissions` | POST | Create permission. |
| `/api/permissions` | GET | List permissions. |
| `/api/permissions/{id}` | PUT | Update permission. |
| `/api/permissions/{id}` | DELETE | Delete permission. |

Request JSON:

```json
{
  "permissionCode": "PRODUCT_CREATE",
  "permissionName": "Create Product",
  "moduleName": "PRODUCT",
  "description": "Allows product creation",
  "active": true
}
```

Response JSON:

```json
{
  "id": 1,
  "permissionCode": "PRODUCT_CREATE",
  "permissionName": "Create Product",
  "moduleName": "PRODUCT",
  "description": "Allows product creation",
  "active": true
}
```

### Roles

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/roles` | POST | Create org-scoped role. |
| `/api/roles` | GET | List org roles. |
| `/api/roles/{id}` | PUT | Update role. |
| `/api/roles/{id}` | DELETE | Delete role. |
| `/api/roles/{roleId}/permissions` | POST | Assign permissions to role. |
| `/api/roles/defaults` | POST | Seed default roles for current org. |
| `/api/roles/defaults/{orgId}` | POST | Seed default roles for given org. |

Request JSON:

```json
{
  "roleName": "STORE_MANAGER",
  "roleLevel": 3,
  "parentRoleId": 2,
  "permissions": ["PRODUCT_VIEW", "BILLING_VIEW", "REPORT_VIEW"],
  "active": true
}
```

Assign permissions request:

```json
{
  "permissions": ["PRODUCT_CREATE", "PRODUCT_UPDATE", "BILLING_CREATE"]
}
```

Response JSON:

```json
{
  "id": 3,
  "orgId": 101,
  "roleName": "STORE_MANAGER",
  "roleLevel": 3,
  "parentRoleId": 2,
  "active": true,
  "permissions": [
    {
      "permissionCode": "REPORT_VIEW"
    }
  ]
}
```

### User Roles

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/users/{userId}/roles` | POST | Assign roles to user for current org. |

Request JSON:

```json
{
  "roleIds": [2, 3]
}
```

Response JSON:

```json
[
  {
    "id": 10,
    "userId": 5,
    "roleId": 2,
    "orgId": 101
  }
]
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Organization Service

Base path: `/api/organizations`

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/organizations` | GET | List organizations. |
| `/api/organizations/{id}` | GET | Get organization. |
| `/api/organizations` | POST | Create organization. |
| `/api/organizations/{id}` | PUT | Update organization. |
| `/api/organizations/{id}` | DELETE | Delete organization. |

Request JSON:

```json
{
  "name": "RBF Retail",
  "code": "RBF"
}
```

Response JSON:

```json
{
  "id": 101,
  "name": "RBF Retail",
  "code": "RBF",
  "createdAt": "2026-05-07T10:30:00",
  "updatedAt": "2026-05-07T10:30:00"
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Product Service

Base path: `/api/products`, gateway path: `/products`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/products` | GET | List products for current org. | Authenticated |
| `/api/products/{id}` | GET | Get product by id for current org. | Authenticated |
| `/api/products/sku/{sku}` | GET | Get product by SKU. | Authenticated |
| `/api/products/barcode/{barcode}` | GET | Get product by barcode. | Authenticated |
| `/api/products/search?sku=SKU` | GET | Search products by SKU. | Authenticated |
| `/api/products` | POST | Create product. | `PRODUCT_CREATE` |
| `/api/products/{id}` | PUT | Update product. | `PRODUCT_UPDATE` |
| `/api/products/{id}` | DELETE | Delete product. | `PRODUCT_DELETE` |

Request JSON:

```json
{
  "sku": "SKU-001",
  "barcode": "8901001000011",
  "name": "Rice 1kg",
  "price": 80.0,
  "gst": 5.0
}
```

Response JSON:

```json
{
  "id": 1,
  "sku": "SKU-001",
  "barcode": "8901001000011",
  "name": "Rice 1kg",
  "price": 80.0,
  "gst": 5.0,
  "orgId": 101
}
```

Error responses: `400` validation, `401`, `403`, `404` product not found for org, `500`.

## Inventory Service

Base path: `/api/inventory`, gateway path: `/inventory`

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/inventory/add` | POST | Add stock. |
| `/api/inventory/deduct` | POST | Deduct stock. |
| `/api/inventory` | GET | List stock. |
| `/api/inventory/page?page=0&size=50` | GET | Paginated stock list. |
| `/api/inventory/{productId}` | GET | Check stock for product. |
| `/api/inventory/alerts` | GET | Scan low/out-of-stock and expiry alerts. |
| `/api/inventory/notifications` | GET | List open inventory notifications. |
| `/api/inventory/notifications/{id}/acknowledge` | POST | Acknowledge notification. |

Request JSON:

```json
{
  "productId": 1,
  "quantity": 25,
  "lowStockThreshold": 10,
  "expiryDate": "2026-12-31"
}
```

Response JSON:

```json
{
  "productId": 1,
  "quantity": 125,
  "lowStockThreshold": 10,
  "expiryDate": "2026-12-31",
  "orgId": 101
}
```

Notification response JSON:

```json
{
  "id": 15,
  "productId": 1,
  "alertType": "LOW_STOCK",
  "message": "Stock is below threshold",
  "status": "OPEN",
  "orgId": 101
}
```

Error responses: `400` invalid quantity or insufficient stock, `401`, `403`, `404`, `500`.

## Billing Service

Base path: `/api/billing`, gateway path: `/billing`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/billing/invoices` | GET | List invoices. | Authenticated |
| `/api/billing/invoices/page?page=0&size=25&status=FINALIZED` | GET | Paginated invoices. | Authenticated |
| `/api/billing/invoices/drafts` | GET | List draft invoices. | Authenticated |
| `/api/billing/invoices/held` | GET | List held invoices. | Authenticated |
| `/api/billing/invoices/{id}` | GET | Get invoice. | Authenticated |
| `/api/billing/invoices/{id}/pdf` | GET | Download invoice PDF. | Authenticated |
| `/api/billing/invoices` | POST | Create and finalize invoice. | `BILLING_CREATE` |
| `/api/billing/invoices/drafts` | POST | Create draft invoice. | `BILLING_CREATE` |
| `/api/billing/invoices/{id}/cart` | PUT | Update draft cart quantity/items. | `BILLING_CREATE` |
| `/api/billing/invoices/{id}/hold` | POST | Hold invoice. | `BILLING_CREATE` |
| `/api/billing/invoices/{id}/resume` | POST | Resume invoice by id. | `BILLING_CREATE` |
| `/api/billing/invoices/held/{holdReference}/resume` | POST | Resume invoice by hold reference. | `BILLING_CREATE` |
| `/api/billing/invoices/{id}/finalize` | POST | Finalize draft and trigger inventory, payment, accounting. | `BILLING_CREATE` |

Create invoice request JSON:

```json
{
  "customerId": 501,
  "creditSale": false,
  "discountPercentage": 5.0,
  "sellerCountry": "IN",
  "sellerRegion": "KA",
  "customerCountry": "IN",
  "customerRegion": "KA",
  "customerType": "B2C",
  "customerTaxExempt": false,
  "reverseChargeApplicable": false,
  "currencyCode": "INR",
  "exchangeRate": 1.0,
  "transactionType": "DOMESTIC",
  "paymentAmount": 900.0,
  "paymentMode": "CASH",
  "items": [
    {
      "productId": 1,
      "sku": "SKU-001",
      "barcode": "8901001000011",
      "quantity": 2,
      "productCategoryId": 10,
      "hsnSacCode": "1006"
    }
  ]
}
```

Finalize request JSON:

```json
{
  "paymentAmount": 900.0,
  "paymentMode": "UPI",
  "creditSale": false
}
```

Hold request JSON:

```json
{
  "holdReference": "COUNTER-1-001",
  "reason": "Customer will return"
}
```

Response JSON:

```json
{
  "id": 1001,
  "status": "FINALIZED",
  "customerId": 501,
  "subtotal": 800.0,
  "discount": 40.0,
  "tax": 38.0,
  "roundOff": 0.0,
  "total": 798.0,
  "orgId": 101,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "price": 400.0,
      "tax": 38.0
    }
  ]
}
```

PDF response: `application/pdf` with `Content-Disposition: attachment; filename="invoice-{id}.pdf"`.

Error responses: `400` invalid cart, stock, tax, or credit limit issue; `401`, `403`, `404`, `500`.

## Payment Service

Base path: `/api/payments`

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/payments` | POST | Record payment or partial payment. |
| `/api/payments/refunds` | POST | Record refund. |
| `/api/payments/invoice/{invoiceId}` | GET | List invoice payments. |
| `/api/payments/invoice/{invoiceId}/status?invoiceTotal=1000` | GET | Get payment status. |
| `/api/payments/invoice/statuses` | POST | Batch payment statuses. |

Record payment request JSON:

```json
{
  "invoiceId": 1001,
  "amount": 500.0,
  "paymentMode": "UPI"
}
```

Refund request JSON:

```json
{
  "invoiceId": 1001,
  "amount": 100.0,
  "reason": "Partial item return"
}
```

Batch status request JSON:

```json
{
  "items": [
    {
      "invoiceId": 1001,
      "invoiceTotal": 798.0
    }
  ]
}
```

Response JSON:

```json
{
  "id": 1,
  "invoiceId": 1001,
  "amount": 500.0,
  "status": "PARTIAL",
  "orgId": 101
}
```

Payment status response JSON:

```json
{
  "invoiceId": 1001,
  "invoiceTotal": 798.0,
  "paidAmount": 500.0,
  "dueAmount": 298.0,
  "status": "PARTIAL"
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Purchase Service

Base path: `/api/purchases`, gateway path: `/purchases`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/purchases` | GET | List purchase orders. | `INVENTORY_UPDATE` or `ACCOUNTING_VIEW` |
| `/api/purchases/{id}` | GET | Get purchase order. | `INVENTORY_UPDATE` or `ACCOUNTING_VIEW` |
| `/api/purchases` | POST | Create purchase order. | `INVENTORY_UPDATE` |
| `/api/purchases/{id}/grn` | POST | Receive goods and increase inventory. | `INVENTORY_UPDATE` |

Create purchase request JSON:

```json
{
  "supplierId": 301,
  "supplierName": "ABC Traders",
  "items": [
    {
      "productId": 1,
      "quantity": 50,
      "unitCost": 60.0
    }
  ]
}
```

GRN request JSON:

```json
{
  "grnNumber": "GRN-2026-001",
  "receivedItems": [
    {
      "productId": 1,
      "quantity": 50
    }
  ]
}
```

Response JSON:

```json
{
  "id": 1,
  "supplierId": 301,
  "supplierName": "ABC Traders",
  "status": "RECEIVED",
  "total": 3000.0,
  "orgId": 101,
  "items": [
    {
      "productId": 1,
      "quantity": 50,
      "unitCost": 60.0
    }
  ]
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Sales Return Service

Base path: `/api/sales-returns`, gateway path: `/sales-returns`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/sales-returns` | GET | List sales returns. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |
| `/api/sales-returns/{id}` | GET | Get sales return. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |
| `/api/sales-returns/invoice/{invoiceId}` | GET | List returns for invoice. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |
| `/api/sales-returns` | POST | Create return, restore inventory, refund, accounting reversal. | `BILLING_CREATE` |

Request JSON:

```json
{
  "invoiceId": 1001,
  "refundAmount": 100.0,
  "reason": "Damaged item",
  "items": [
    {
      "invoiceItemId": 1,
      "productId": 1,
      "quantity": 1,
      "refundAmount": 100.0
    }
  ]
}
```

Response JSON:

```json
{
  "id": 1,
  "invoiceId": 1001,
  "refundAmount": 100.0,
  "refundStatus": "REFUNDED",
  "orgId": 101,
  "items": [
    {
      "productId": 1,
      "quantity": 1,
      "refundAmount": 100.0
    }
  ]
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Customer Credit Service

Base path: `/api/customer-credits`, gateway path: `/customer-credits`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/customer-credits` | GET | List credit accounts. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |
| `/api/customer-credits/customers/{customerId}` | GET | Get customer credit account. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |
| `/api/customer-credits/accounts` | POST | Create or update credit account. | `ACCOUNTING_VIEW` or `BILLING_CREATE` |
| `/api/customer-credits/validate` | POST | Validate credit limit. | `BILLING_CREATE` |
| `/api/customer-credits/sales` | POST | Record credit sale. | `BILLING_CREATE` |
| `/api/customer-credits/customers/{customerId}/settlements` | POST | Settle customer dues. | `BILLING_CREATE` or `ACCOUNTING_VIEW` |
| `/api/customer-credits/customers/{customerId}/transactions` | GET | List credit transactions. | `BILLING_VIEW` or `ACCOUNTING_VIEW` |

Credit account request JSON:

```json
{
  "customerId": 501,
  "customerName": "Retail Customer",
  "creditLimit": 10000.0
}
```

Credit validation request JSON:

```json
{
  "customerId": 501,
  "amount": 1500.0
}
```

Credit sale request JSON:

```json
{
  "customerId": 501,
  "invoiceId": 1001,
  "amount": 1500.0
}
```

Settlement request JSON:

```json
{
  "amount": 500.0,
  "paymentMode": "CASH",
  "reference": "RCPT-001"
}
```

Response JSON:

```json
{
  "customerId": 501,
  "customerName": "Retail Customer",
  "creditLimit": 10000.0,
  "outstandingAmount": 1000.0,
  "availableCredit": 9000.0,
  "orgId": 101
}
```

Error responses: `400` credit limit exceeded or invalid amount, `401`, `403`, `404`, `500`.

## Tax Service

Base paths: `/tax`, `/invoices/{invoiceId}/tax-breakup`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/tax/countries` | POST | Create tax country. | `ACCOUNTING_VIEW` |
| `/tax/countries` | GET | List tax countries. | Authenticated |
| `/tax/regions` | POST | Create region. | `ACCOUNTING_VIEW` |
| `/tax/regions` | GET | List regions. | Authenticated |
| `/tax/regimes` | POST | Create regime. | `ACCOUNTING_VIEW` |
| `/tax/regimes` | GET | List regimes. | Authenticated |
| `/tax/slabs` | POST | Create tax slab. | `ACCOUNTING_VIEW` |
| `/tax/slabs` | GET | List tax slabs for org/global filters. | Authenticated |
| `/tax/slabs/{id}` | PUT | Update tax slab. | `ACCOUNTING_VIEW` |
| `/tax/slabs/{id}` | DELETE | Delete tax slab. | `ACCOUNTING_VIEW` |
| `/tax/rules` | POST | Create tax rule. | `ACCOUNTING_VIEW` |
| `/tax/rules` | GET | List tax rules. | Authenticated |
| `/tax/rules/{id}` | PUT | Update tax rule. | `ACCOUNTING_VIEW` |
| `/tax/rules/{id}` | DELETE | Delete tax rule. | `ACCOUNTING_VIEW` |
| `/tax/calculate` | POST | Calculate India GST, VAT, sales tax, export, exempt, reverse charge. | Authenticated |
| `/tax/invoice-breakups` | POST | Save invoice tax breakup. | Authenticated |
| `/invoices/{invoiceId}/tax-breakup` | GET | Get saved tax breakup. | Authenticated |

Tax slab request JSON:

```json
{
  "orgId": 101,
  "countryCode": "IN",
  "regionCode": "KA",
  "taxRegimeId": 1,
  "taxName": "CGST 2.5%",
  "taxType": "CGST",
  "taxRate": 2.5,
  "hsnSacCode": "1006",
  "productCategoryId": 10,
  "effectiveFrom": "2026-04-01",
  "effectiveTo": "2027-03-31",
  "active": true
}
```

Tax rule request JSON:

```json
{
  "orgId": 101,
  "sourceCountry": "IN",
  "sourceRegion": "KA",
  "destinationCountry": "IN",
  "destinationRegion": "MH",
  "taxRegimeId": 1,
  "taxType": "IGST",
  "transactionType": "DOMESTIC",
  "customerType": "B2C",
  "productCategoryId": 10,
  "hsnSacCode": "1006",
  "priority": 1,
  "active": true
}
```

Calculate request JSON:

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

Calculate response JSON:

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

Save invoice breakup request JSON:

```json
{
  "invoiceId": 1001,
  "invoiceItemId": 1,
  "breakups": [
    {
      "taxName": "CGST",
      "taxType": "CGST",
      "taxRate": 2.5,
      "taxableAmount": 760.0,
      "taxAmount": 19.0,
      "countryCode": "IN",
      "regionCode": "KA"
    }
  ]
}
```

Error responses: `400` invalid country/rate/effective dates/rule conflict, `401`, `403`, `404`, `500`.

## Report Service

Base path: `/api/reports`, gateway path: `/reports`

| Endpoint | Method | Description | Permission |
| --- | --- | --- | --- |
| `/api/reports/sales` | GET | Sales report from Billing Service. | `REPORT_VIEW` |
| `/api/reports/inventory` | GET | Inventory report from Inventory Service. | `REPORT_VIEW` |
| `/api/reports/financial` | GET | Financial report from Accounting Service. | `REPORT_VIEW` |
| `/api/reports/dashboard?lowStockThreshold=10&topLimit=5` | GET | Combined dashboard analytics. | `REPORT_VIEW` |
| `/api/reports/dashboard/daily-sales` | GET | Daily sales metric. | `REPORT_VIEW` |
| `/api/reports/dashboard/monthly-revenue` | GET | Monthly revenue metric. | `REPORT_VIEW` |
| `/api/reports/dashboard/top-selling-products?limit=10` | GET | Top selling products. | `REPORT_VIEW` |
| `/api/reports/dashboard/low-stock-alerts?threshold=10` | GET | Low stock alerts. | `REPORT_VIEW` |
| `/api/reports/dashboard/outstanding-payments` | GET | Outstanding payments. | `REPORT_VIEW` |

Dashboard response JSON:

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

Error responses: `400`, `401`, `403`, `404`, `500`, `503` downstream service unavailable.

## Order Service

Base path: `/api/orders`

| Endpoint | Method | Description |
| --- | --- | --- |
| `/api/orders` | GET | List customer orders for current org. |
| `/api/orders/{id}` | GET | Get order by id. |
| `/api/orders` | POST | Create customer order. |

Request JSON:

```json
{
  "customerName": "Retail Customer",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Response JSON:

```json
{
  "id": 1,
  "customerName": "Retail Customer",
  "status": "CREATED",
  "orgId": 101
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.

## Accounting Service

Gateway path: `/accounting`

The gateway is configured to route `/accounting/**` to Accounting Service. The current workspace does not contain an Accounting Service controller module yet, so endpoint-level documentation should be added when that controller is generated.

Expected ledger entry request JSON:

```json
{
  "invoiceId": 1001,
  "entryType": "SALE",
  "debitAccount": "Cash",
  "creditAccount": "Sales Revenue",
  "amount": 798.0,
  "taxAmount": 38.0
}
```

Expected response JSON:

```json
{
  "id": 1,
  "invoiceId": 1001,
  "entryType": "SALE",
  "amount": 798.0,
  "orgId": 101
}
```

Error responses: `400`, `401`, `403`, `404`, `500`.
