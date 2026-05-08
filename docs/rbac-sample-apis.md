# RBAC Sample APIs

All requests except login require:

```http
Authorization: Bearer <jwt>
X-ORG-ID: 101
```

## Create Role

```http
POST /api/roles
```

```json
{
  "roleName": "STORE_MANAGER",
  "roleLevel": 3,
  "parentRoleId": 2,
  "permissions": ["PRODUCT_VIEW", "BILLING_VIEW", "REPORT_VIEW"]
}
```

## Assign Permissions

```http
POST /api/roles/2/permissions
```

```json
{
  "permissions": ["PRODUCT_CREATE", "PRODUCT_UPDATE", "BILLING_CREATE"]
}
```

## Assign Role To User

```http
POST /api/users/1/roles
```

```json
{
  "roleIds": [2, 3]
}
```

## Login Response JWT Claims

```json
{
  "userId": 1,
  "username": "admin",
  "org_id": 101,
  "roles": ["ADMIN"],
  "permissions": ["PRODUCT_CREATE", "PRODUCT_VIEW", "BILLING_CREATE"]
}
```
