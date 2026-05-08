# Production Deployment Guide

This guide covers production deployment for the pure Spring Boot retail billing microservices. The system uses independent services, independent databases, REST communication, JWT security, and `org_id` based multi-tenancy.

## Environment Variables

Use environment variables or a secure secret manager. Do not commit production secrets.

### Common Service Variables

Apply these to every Spring Boot service:

```bash
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=9102
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_OPEN_IN_VIEW=false
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_RBF_PRODUCT=INFO
```

Database-backed services:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://<mysql-host>:3306/<database>?useSSL=true&allowPublicKeyRetrieval=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=<service_user>
SPRING_DATASOURCE_PASSWORD=<strong_password>
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect
```

JWT settings:

```bash
JWT_SECRET=<minimum-32-character-random-secret>
JWT_EXPIRATION_MINUTES=60
GATEWAY_JWT_SECRET=<same-secret-used-by-auth-service>
```

Tenant header:

```bash
TENANT_HEADER=X-ORG-ID
```

### API Gateway Variables

```bash
SERVER_PORT=9100
GATEWAY_JWT_ENABLED=true
GATEWAY_JWT_SECRET=<same-secret-used-by-auth-service>
GATEWAY_ROUTES_AUTH_TARGET_URL=http://auth-service:9104/api/auth
GATEWAY_ROUTES_PRODUCTS_TARGET_URL=http://product-service:9102/api/products
GATEWAY_ROUTES_BILLING_TARGET_URL=http://billing-service:9105/api/billing
GATEWAY_ROUTES_INVENTORY_TARGET_URL=http://inventory-service:9106/api/inventory
GATEWAY_ROUTES_PURCHASES_TARGET_URL=http://purchase-service:9109/api/purchases
GATEWAY_ROUTES_SALES_RETURNS_TARGET_URL=http://sales-return-service:9110/api/sales-returns
GATEWAY_ROUTES_CUSTOMER_CREDITS_TARGET_URL=http://customer-credit-service:9111/api/customer-credits
GATEWAY_ROUTES_TAX_TARGET_URL=http://tax-service:9112/tax
GATEWAY_ROUTES_REPORTS_TARGET_URL=http://report-service:9113/api/reports
GATEWAY_ROUTES_ACCOUNTING_TARGET_URL=http://accounting-service:9107/api/accounting
```

### Billing Service Variables

```bash
SERVER_PORT=9105
SERVICES_PRODUCT_SERVICE_BASE_URL=http://product-service:9102
SERVICES_INVENTORY_SERVICE_BASE_URL=http://inventory-service:9106
SERVICES_PAYMENT_SERVICE_BASE_URL=http://payment-service:9108
SERVICES_ACCOUNTING_SERVICE_BASE_URL=http://accounting-service:9107
SERVICES_CUSTOMER_CREDIT_SERVICE_BASE_URL=http://customer-credit-service:9111
SERVICES_TAX_SERVICE_BASE_URL=http://tax-service:9112
INVOICE_COMPANY_NAME=RBF Retail Pvt Ltd
INVOICE_COMPANY_ADDRESS=<registered-address>
INVOICE_COMPANY_PHONE=<support-phone>
INVOICE_COMPANY_EMAIL=<billing-email>
INVOICE_COMPANY_GSTIN=<gstin-or-tax-id>
```

### Report Service Variables

```bash
SERVER_PORT=9113
SERVICES_BILLING_SERVICE_BASE_URL=http://billing-service:9105
SERVICES_INVENTORY_SERVICE_BASE_URL=http://inventory-service:9106
SERVICES_ACCOUNTING_SERVICE_BASE_URL=http://accounting-service:9107
SERVICES_PAYMENT_SERVICE_BASE_URL=http://payment-service:9108
```

### Purchase, Sales Return, and Order Variables

```bash
SERVICES_INVENTORY_SERVICE_BASE_URL=http://inventory-service:9106
SERVICES_BILLING_SERVICE_BASE_URL=http://billing-service:9105
SERVICES_PAYMENT_SERVICE_BASE_URL=http://payment-service:9108
SERVICES_ACCOUNTING_SERVICE_BASE_URL=http://accounting-service:9107
SERVICES_PRODUCT_SERVICE_BASE_URL=http://product-service:9102
```

## Production Properties

Create `application-prod.yml` per service or provide the equivalent values as environment variables.

### Common Production Template

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

server:
  shutdown: graceful
  servlet:
    context-path: /

logging:
  level:
    root: INFO
    com.rbf.product: INFO
```

### API Gateway Production Properties

```yaml
gateway:
  jwt:
    enabled: true
    secret: ${GATEWAY_JWT_SECRET}
  routes:
    auth:
      target-url: ${GATEWAY_ROUTES_AUTH_TARGET_URL}
    products:
      target-url: ${GATEWAY_ROUTES_PRODUCTS_TARGET_URL}
    billing:
      target-url: ${GATEWAY_ROUTES_BILLING_TARGET_URL}
    inventory:
      target-url: ${GATEWAY_ROUTES_INVENTORY_TARGET_URL}
    purchases:
      target-url: ${GATEWAY_ROUTES_PURCHASES_TARGET_URL}
    sales-returns:
      target-url: ${GATEWAY_ROUTES_SALES_RETURNS_TARGET_URL}
    customer-credits:
      target-url: ${GATEWAY_ROUTES_CUSTOMER_CREDITS_TARGET_URL}
    tax:
      target-url: ${GATEWAY_ROUTES_TAX_TARGET_URL}
    reports:
      target-url: ${GATEWAY_ROUTES_REPORTS_TARGET_URL}
    accounting:
      target-url: ${GATEWAY_ROUTES_ACCOUNTING_TARGET_URL}
```

### Database Migration Policy

Production should not rely on Hibernate schema updates.

- Use `ddl-auto=validate`.
- Run reviewed SQL migration scripts before service deployment.
- Keep schema changes backward compatible during rolling deployments.
- Add indexes before releasing query-heavy features.
- Validate every tenant table has `org_id` and indexes using `org_id`.

## Security Recommendations

### Authentication and Authorization

- Use a long random JWT secret, at least 32 characters.
- Keep the same JWT secret in Auth Service and API Gateway.
- Use short token expiry in production, for example 30 to 60 minutes.
- Store role and permission assignments in the Auth Service database.
- Enforce `@PreAuthorize` permissions on every sensitive write or report API.
- Include `org_id`, roles, and permissions in JWT claims.
- Reject requests missing `X-ORG-ID` for tenant-scoped APIs.
- Verify the authenticated JWT `org_id` matches the `X-ORG-ID` header.

### Network Security

- Expose only API Gateway to the public network.
- Keep all service ports and MySQL ports private.
- Use TLS termination at the ingress/load balancer.
- Use HTTPS from clients to Gateway.
- Prefer private networking for Gateway-to-service calls.
- Do not expose MySQL containers directly outside the private network.

### Secrets

- Never store production passwords in `docker-compose.yml`.
- Use environment-specific secret storage.
- Rotate database passwords and JWT secrets on a schedule.
- Avoid logging tokens, passwords, card data, and full customer tax identifiers.

### Database Security

- Use one database and one DB user per service.
- Grant only required privileges to each service user.
- Enable MySQL TLS where supported.
- Enable backups and point-in-time recovery.
- Encrypt database volumes/disks.

### Application Hardening

- Disable debug logs in production.
- Add request size limits at the gateway or ingress.
- Add rate limiting at the ingress or gateway layer.
- Validate every request DTO.
- Sanitize exception messages before returning them to clients.
- Audit billing, payment, inventory, role, and tax configuration changes.

## Deployment Order

Deploy infrastructure first, then data services, then dependent services, then gateway.

1. Provision private network, ingress, TLS certificate, and DNS.
2. Provision MySQL databases and service-specific users.
3. Apply database schema scripts and indexes.
4. Deploy Auth Service.
5. Seed SUPER_ADMIN, default permissions, and required roles.
6. Deploy Organization Service.
7. Deploy Product Service.
8. Deploy Inventory Service.
9. Deploy Payment Service.
10. Deploy Tax Service.
11. Deploy Customer Credit Service.
12. Deploy Purchase Service.
13. Deploy Sales Return Service.
14. Deploy Billing Service.
15. Deploy Report Service.
16. Deploy Order Service if used.
17. Deploy Accounting Service when available.
18. Deploy API Gateway last.
19. Run smoke tests through the gateway.

Smoke test sequence:

```text
POST /auth/login
POST /products
POST /inventory/add
POST /billing/invoices
GET /payments/invoice/{invoiceId}/status
GET /reports/dashboard
```

## Backup Strategy

Each service owns its database, so backup and restore must be service-aware.

### Backup Scope

Back up these databases independently:

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
- `rbf_accounting` when Accounting Service is added

### Backup Schedule

- Full backup: daily.
- Incremental or binary log backup: every 15 to 60 minutes.
- Retention: at least 30 days for operational recovery.
- Long-term retention: monthly snapshots for compliance, if required.
- Store backups in a different availability zone or storage account.

### Backup Commands

Example logical backup:

```bash
mysqldump \
  --single-transaction \
  --routines \
  --triggers \
  --set-gtid-purged=OFF \
  -h <mysql-host> \
  -u <backup-user> \
  -p rbf_billing > rbf_billing_$(date +%F).sql
```

Example restore:

```bash
mysql -h <mysql-host> -u <restore-user> -p rbf_billing < rbf_billing_2026-05-07.sql
```

### Consistency Notes

- Billing, Payment, Inventory, Accounting, and Sales Return data are related across services.
- For point-in-time restore, restore related service databases to the same timestamp.
- Keep invoice IDs and accounting references stable.
- Test restore procedures monthly in a non-production environment.

### Backup Verification

After each backup cycle:

- Verify backup file exists and has non-zero size.
- Run checksum validation.
- Restore the latest backup into a staging database.
- Run smoke queries against tenant-filtered tables.
- Confirm `org_id` indexes exist after restore.

## Rollback Strategy

- Keep the previous container image tag available.
- Do not deploy irreversible database changes without a tested rollback plan.
- Prefer additive schema changes: add new columns/tables first, deploy code, then clean up later.
- If deployment fails before schema changes are used, roll back service image.
- If data writes occurred with the new version, perform a forward fix unless a full point-in-time restore is explicitly approved.

## Production Checklist

- `SPRING_PROFILES_ACTIVE=prod` set for all services.
- `ddl-auto=validate` set for all database services.
- JWT secret is strong and shared only where required.
- Only API Gateway is publicly exposed.
- MySQL ports are private.
- Service databases and users are isolated.
- Backups are scheduled and restore-tested.
- Monitoring captures API errors, latency, DB pool usage, and JVM memory.
- Logs redact secrets and customer-sensitive data.
- Smoke tests pass through API Gateway.
