# International Tax Engine Examples

All requests include `X-ORG-ID`.

## 1. India Intra-State GST
```json
{
  "sellerCountry": "INDIA",
  "sellerRegion": "KA",
  "customerCountry": "INDIA",
  "customerRegion": "KA",
  "customerType": "B2C",
  "productId": 1,
  "productCategoryId": 10,
  "hsnSacCode": "1001",
  "quantity": 2,
  "unitPrice": 1000,
  "discountAmount": 0,
  "currencyCode": "INR",
  "exchangeRate": 1,
  "transactionType": "DOMESTIC"
}
```
Response applies `CGST` plus `SGST`.

## 2. India Inter-State GST
Change `customerRegion` to `MH`. Response applies `IGST`.

## 3. UK VAT
```json
{
  "sellerCountry": "UK",
  "sellerRegion": "LND",
  "customerCountry": "UK",
  "customerRegion": "LND",
  "customerType": "B2C",
  "productId": 2,
  "productCategoryId": 20,
  "quantity": 1,
  "unitPrice": 100,
  "discountAmount": 0,
  "currencyCode": "GBP",
  "exchangeRate": 1,
  "transactionType": "DOMESTIC"
}
```
Response applies configured `VAT`.

## 4. USA Sales Tax
Use `sellerCountry` and `customerCountry` as `USA`, with `customerRegion` as the destination state. Response applies configured `SALES_TAX`.

## 5. Export Zero-Rated Invoice
Use different seller/customer countries with a `ZERO_RATED` export rule. Response returns zero tax.

## 6. Tax-Exempt Customer Invoice
Set `"customerTaxExempt": true`. Response uses `EXEMPT` with zero tax.

## 7. Reverse Charge Invoice
Set `"reverseChargeApplicable": true`. Response uses `REVERSE_CHARGE`; tax is not collected from the customer.

## Billing Credit
Billing request supports international tax fields at invoice level and `productCategoryId` / `hsnSacCode` at item level. Billing calls `POST /tax/calculate`, saves `invoice_tax_breakup`, and posts ledger entries through the existing accounting REST endpoint.
