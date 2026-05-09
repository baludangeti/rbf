# Functional Test Troubleshooting Guide

## Service Not Reachable

1. Run `../health-check.bat`.
2. Check `../logs/<service-name>.log`.
3. Confirm the service port matches:
   - gateway `8080`
   - auth `8081`
   - organization `8082`
   - product `8083`
   - inventory `8084`
   - billing `8085`
   - payment `8086`
   - accounting `8087`
   - report `8088`
   - tax `8089`
   - console `8090`

## Database Failure

1. Confirm MySQL is listening on `localhost:3306`.
2. Confirm `DB_USERNAME` and `DB_PASSWORD`, or use the local defaults in each `application.yml`.
3. Re-run `sql/functional-seed-data.sql`.

## Unauthorized Requests

1. Confirm the collection login request succeeded.
2. Confirm `jwtToken` and `orgId` variables are set.
3. Confirm downstream requests send:

```http
Authorization: Bearer {{jwtToken}}
X-ORG-ID: {{orgId}}
```

## Multi-Tenant Failures

1. Confirm every tenant-owned table has `org_id`.
2. Confirm repositories filter by `org_id`.
3. Use Org A token/header for Org A data and Org B token/header for Org B data.

## Billing Failures

Check dependent services first:

- product-service
- inventory-service
- payment-service
- accounting-service
- tax-service

Billing must not create a final invoice if stock deduction, tax calculation, payment, or ledger posting fails.
