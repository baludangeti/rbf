# RBF Product Platform

Pure Spring Boot microservice scaffold.

## Rules followed

- No Eureka.
- No Spring Cloud.
- No Kafka.
- No third-party runtime services.
- Every service runs independently.
- Every service owns its own database.
- Services communicate only through REST APIs.
- Service-to-service calls use `RestTemplate`.
- Every persisted table includes `org_id`.
- Reads and writes are filtered by `org_id` from the `X-Org-Id` request header.

## Services

| Service | Port | Database |
| --- | ---: | --- |
| organization-service | 9101 | `./data/organization-db` |
| product-service | 9102 | `./data/product-db` |
| order-service | 9103 | `./data/order-db` |

## Run

Build everything:

```powershell
mvn clean package
```

Run a service independently:

```powershell
mvn -pl organization-service spring-boot:run
mvn -pl product-service spring-boot:run
mvn -pl order-service spring-boot:run
```

All business endpoints require:

```http
X-Org-Id: 1
```
