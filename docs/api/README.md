# Retail Billing and Financial System API Documentation

This folder contains the API documentation for the pure Spring Boot retail billing microservices.

## Files

- [all-services.md](all-services.md) - complete endpoint catalog for every service.

## Common Headers

Tenant-aware APIs require these headers:

```http
Authorization: Bearer <jwt-token>
X-ORG-ID: 101
Content-Type: application/json
```

The API Gateway validates JWT tokens and forwards `X-ORG-ID` to downstream services. Services also filter tenant data using `org_id`.

## Common Success Wrapper

Where the common wrapper is used, responses follow:

```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {},
  "timestamp": "2026-05-07T10:30:00"
}
```

Several current controllers return DTOs/entities directly. The payload examples in [all-services.md](all-services.md) document the actual response body shape for those APIs.

## Common Error Response

All services use the same global exception format:

```json
{
  "timestamp": "2026-05-07T10:30:00",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/products",
  "errors": {
    "name": "must not be blank",
    "price": "must be greater than 0"
  }
}
```

Common error statuses:

- `400 Bad Request` - validation failure or invalid input.
- `401 Unauthorized` - missing or invalid JWT.
- `403 Forbidden` - authenticated user lacks required permission.
- `404 Not Found` - resource does not exist for the current `org_id`.
- `500 Internal Server Error` - unexpected service error.
