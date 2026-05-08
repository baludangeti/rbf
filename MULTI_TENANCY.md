# Multi-Tenant Support

All tenant-aware APIs require this header:

```http
X-ORG-ID: 1
```

## Filter

`OrgContextFilter` extracts `X-ORG-ID`, validates it, and stores it in `OrgContext` for the current request thread.

```java
String orgHeader = request.getHeader("X-ORG-ID");
OrgContext.setOrgId(Long.parseLong(orgHeader));
```

Register the filter in each service:

```java
@SpringBootApplication
@Import(OrgFilterConfig.class)
public class ProductServiceApplication {
}
```

## Service Layer

Use `OrgContextResolver` to pass `org_id` into service logic:

```java
Long orgId = orgContextResolver.currentOrgId();
return productRepository.findByIdAndOrgId(id, orgId);
```

## Repository

Never query by `id` alone. Always include `orgId`:

```java
List<Product> findByOrgIdOrderByNameAsc(Long orgId);

Optional<Product> findByIdAndOrgId(Long id, Long orgId);
```

## Entity

All tenant tables include `org_id` through `OrgScopedEntity`:

```java
@Column(name = "org_id", nullable = false)
private Long orgId;
```
