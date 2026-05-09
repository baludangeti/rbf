# Retail Billing Functional Test Pack

This pack validates real business flows for the Retail Billing and Financial microservices system. It complements unit and MockMvc tests with API-level Postman/Newman runs, seed SQL, database verification queries, and JSP console smoke checks.

## Files

- `test-plan.md` contains the module-by-module functional test case table.
- `execution-guide.md` contains the step-by-step runbook.
- `expected-results.md` lists the expected outcomes after a passing run.
- `troubleshooting.md` covers the common failure modes.
- `postman/retail-billing-functional-tests.postman_collection.json` contains the executable API and JSP smoke checks.
- `sql/functional-seed-data.sql` and `sql/db-verification-queries.sql` prepare and verify business data.

## Scope

Required projects:

- `api-gateway`
- `auth-service`
- `organization-service`
- `product-service`
- `inventory-service`
- `billing-service`
- `payment-service`
- `accounting-service`
- `report-service`
- `tax-service`
- `console-web`

Adjacent retail modules such as purchase, sales return, customer, supplier, and customer credit are included in the test plan as business scenarios. If those modules are running, add their base URLs to the Postman environment and enable the optional requests.

## Execution Order

1. Start MySQL.
2. Apply seed SQL from `sql/functional-seed-data.sql`.
3. Build and start services with `../start-all.bat` or `../start-all.sh`.
4. Confirm health with `../health-check.bat` or `../health-check.sh`.
5. Run Newman:

```bash
newman run functional-tests/postman/retail-billing-functional-tests.postman_collection.json -e functional-tests/postman/local.postman_environment.json --reporters cli,json --reporter-json-export functional-tests/reports/newman-result.json
```

6. Run JUnit/MockMvc tests:

```bash
../rbf-gateway/mvnw -f pom.xml -pl api-gateway,auth-service,accounting-service,console-web -am test
```

## Expected Global Results

- Health endpoint for every required service returns `UP`.
- Login returns a JWT containing `userId`, `username`, `org_id`, `roles`, and `permissions`.
- Protected gateway requests without JWT return `401`.
- Protected gateway requests with invalid JWT return `401`.
- Every tenant-owned record stores `org_id`.
- Org A data is not visible through Org B requests.
- Billing creates invoice, invoice item, payment, ledger, tax breakup, and inventory deduction records.
