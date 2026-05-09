# Expected Functional Results

## Organization and Security

- A new organization can be created with a unique code/email.
- Duplicate organization code/email is rejected.
- First ADMIN user can be created for the organization.
- Login returns JWT claims for `userId`, `username`, `org_id`, `roles`, and `permissions`.
- Missing or invalid tokens fail with `401`.
- Missing permissions fail with `403` where `@PreAuthorize` is active.

## Product, Inventory, and Tax

- Product category and product rows are stored with `org_id`.
- Product search works by name, SKU, and barcode.
- Deactivated products are not treated as active sellable products.
- Inventory add/deduct updates only the current tenant stock.
- Negative stock is rejected.
- Intra-state GST returns CGST and SGST breakups.
- Inter-state GST returns IGST breakup.
- Invoice tax breakup rows are stored with invoice and item references.

## Billing, Payment, and Accounting

- Invoice creation saves invoice header and invoice items.
- Inventory is deducted after invoice creation.
- Payment row is saved with payment status.
- Accounting ledger entries are posted for sale, tax payable, and payment.
- Invoice number is generated and invoice view is retrievable.
- Partial payment and credit flows update outstanding balances.

## Returns, Purchase, and Credit

- Sales return supports partial item return and rejects over-return.
- Sales return restores inventory, creates refund payment, and posts reversal ledger entries.
- Purchase order and GRN increase stock and create supplier payable ledger entries.
- Purchase return reverses stock/payable impact.
- Customer credit limit is enforced before credit sale.
- Settlement reduces customer outstanding balance.

## Reports and Console

- Sales, GST, inventory, payment, customer credit, and dashboard reports return tenant-filtered data.
- JSP pages render without server errors.
- Static CSS/JS assets load.
- Protected console pages redirect to login when session is absent.
- Console AJAX calls include `Authorization: Bearer <token>` and `X-ORG-ID`.

## Multi-Tenant Isolation

- Org A product, inventory, invoice, payment, ledger, and report data is not visible to Org B.
- Cross-org reads by ID return `404`, `403`, or an empty same-tenant result.
- Every tenant-owned table stores and filters by `org_id`.
