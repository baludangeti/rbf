# Docker Setup

This setup runs the Spring Boot microservices independently and uses one MySQL container per database-owning service.

## Files

- Root compose file: `docker-compose.yml`
- Build ignore file: `.dockerignore`
- Service Dockerfiles:
  - `api-gateway/Dockerfile`
  - `auth-service/Dockerfile`
  - `organization-service/Dockerfile`
  - `product-service/Dockerfile`
  - `order-service/Dockerfile`
  - `billing-service/Dockerfile`
  - `inventory-service/Dockerfile`
  - `payment-service/Dockerfile`
  - `purchase-service/Dockerfile`
  - `sales-return-service/Dockerfile`
  - `customer-credit-service/Dockerfile`
  - `tax-service/Dockerfile`
  - `report-service/Dockerfile`

`common-service` is a shared library module, so it is copied into each Docker build but does not have its own runtime container.

## Run

```bash
docker compose up --build
```

Run in the background:

```bash
docker compose up -d --build
```

Stop containers:

```bash
docker compose down
```

Stop and delete database volumes:

```bash
docker compose down -v
```

## Public Ports

| Service | Port |
| --- | --- |
| API Gateway | `9100` |
| Organization Service | `9101` |
| Product Service | `9102` |
| Order Service | `9103` |
| Auth Service | `9104` |
| Billing Service | `9105` |
| Inventory Service | `9106` |
| Payment Service | `9108` |
| Purchase Service | `9109` |
| Sales Return Service | `9110` |
| Customer Credit Service | `9111` |
| Tax Service | `9112` |
| Report Service | `9113` |

## MySQL Containers

Each data-owning service has its own MySQL container, database, and Docker volume:

| Service | MySQL container | Database | Volume |
| --- | --- | --- | --- |
| Auth | `auth-mysql` | `rbf_auth` | `auth-mysql-data` |
| Organization | `organization-mysql` | `rbf_organization` | `organization-mysql-data` |
| Product | `product-mysql` | `rbf_product` | `product-mysql-data` |
| Order | `order-mysql` | `rbf_order` | `order-mysql-data` |
| Billing | `billing-mysql` | `rbf_billing` | `billing-mysql-data` |
| Inventory | `inventory-mysql` | `rbf_inventory` | `inventory-mysql-data` |
| Payment | `payment-mysql` | `rbf_payment` | `payment-mysql-data` |
| Purchase | `purchase-mysql` | `rbf_purchase` | `purchase-mysql-data` |
| Sales Return | `sales-return-mysql` | `rbf_sales_return` | `sales-return-mysql-data` |
| Customer Credit | `customer-credit-mysql` | `rbf_customer_credit` | `customer-credit-mysql-data` |
| Tax | `tax-mysql` | `rbf_tax` | `tax-mysql-data` |

## Network

All containers join the `rbf-network` bridge network. Service-to-service REST URLs use Docker DNS names:

```text
http://product-service:9102
http://inventory-service:9106
http://payment-service:9108
http://tax-service:9112
```

The API Gateway forwards external requests to downstream service names inside this network.

## Notes

- Local development can still use the existing H2/MySQL `application.yml` defaults.
- Docker overrides datasource settings with environment variables.
- MySQL credentials in `docker-compose.yml` are development defaults and should be moved to environment-specific secrets for production.
- The gateway has an accounting route placeholder, but the current workspace does not yet contain a runnable Accounting Service module.
