# Functional Test Execution Guide

## Prerequisites

- MySQL running on `localhost:3306`.
- Local database credentials match each service configuration, or environment variables override them.
- Java and Maven wrapper are available.
- Newman is available through `newman` or `npx --yes newman`.

## 1. Prepare Databases

Run the seed script once before a clean functional run:

```bash
mysql -u root -p < functional-tests/sql/functional-seed-data.sql
```

The script creates two tenant datasets, default roles, permissions, products, GST slabs, inventory stock, customers, and suppliers where the backing tables are available.

## 2. Start Services

From the repository root:

```bash
start-all.bat
```

Linux/Mac:

```bash
./start-all.sh
```

Wait until the scripts finish launching the services. Logs are written to `logs/<service-name>.log`.

## 3. Verify Health

```bash
health-check.bat
```

Linux/Mac:

```bash
./health-check.sh
```

Expected result: every required service prints `UP`.

## 4. Run API Functional Tests

```bash
npx --yes newman run functional-tests/postman/retail-billing-functional-tests.postman_collection.json -e functional-tests/postman/local.postman_environment.json --reporters cli,json --reporter-json-export functional-tests/reports/newman-result.json
```

Expected result: Newman exits with code `0`. The collection validates organization registration, login/security, product, inventory, tax, billing, payment, accounting, reporting, JSP page smoke checks, tenant isolation, and negative cases.

## 5. Run JUnit and MockMvc Tests

```bash
D:\Products\rbf-erp\rbf-gateway\mvnw.cmd -f D:\Products\rbf-erp\product\pom.xml -pl api-gateway,auth-service,accounting-service,console-web -am test
```

Expected result: Maven reports `BUILD SUCCESS`.

## 6. Validate Database Side Effects

After Newman completes, run:

```bash
mysql -u root -p < functional-tests/sql/db-verification-queries.sql
```

Expected result: tenant-owned rows have `org_id`, invoice rows have items/tax/payment/ledger side effects, and cross-org checks do not leak Org A data into Org B queries.

## 7. Optional Retail Modules

If `purchase-service`, `sales-return-service`, `customer-service`, `customer-credit-service`, or supplier APIs are running, execute the optional requests described in `test-plan.md` using the same `Authorization` and `X-ORG-ID` headers. These modules are included in the plan because they are part of the retail business flow, even though they are outside the required 11-service startup list.
