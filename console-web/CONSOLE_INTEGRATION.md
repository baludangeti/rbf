# JSP Web Console Integration

## Updated Folder Structure

```text
src/main/webapp/WEB-INF/views/
  layouts/
    app-start.jsp
    app-end.jsp
    header.jsp
    sidebar.jsp
    footer.jsp
    scripts.jsp
  components/
    alert.jsp
    breadcrumb.jsp
    pagination.jsp
    form-errors.jsp
    confirm-modal.jsp
    loader.jsp
  auth/
  organization/
  console/
  billing/
  product/
  inventory/
  customer/
  supplier/
  purchase/
  reports/

src/main/webapp/assets/
  css/
    console.css
    app.css
    invoice-print.css
  js/
    common.js
    validation.js
    console.js
    admin-console.js
    billing-pos.js
    product.js
    inventory.js
    customer.js
    supplier.js
    purchase.js
    sales-return.js
    reports-console.js
    role-permission.js
    settings.js
```

## Updated Controllers

- `LoginController` stores `JWT_TOKEN`, `ORG_ID`, `ORG_NAME`, `USER_ID`, `USERNAME`, `ROLES`, and `PERMISSIONS` in the HTTP session.
- `GatewayAjaxProxyController` forwards browser AJAX calls through the custom API gateway and preserves backend HTTP status codes.
- `ConsoleAjaxExceptionHandler` converts downstream 401, 403, 500, and unavailable-service failures into standard JSON responses for AJAX callers.
- All module controllers continue returning JSP views under the common `layouts/app-start.jsp` and `layouts/app-end.jsp` shell.

## Common RestTemplate Client

`BackendClient` centralizes GET/POST/PUT/DELETE calls from console controllers to backend services. It always uses `BackendHeaders.json(session)`, which includes:

```http
Authorization: Bearer JWT_TOKEN
X-ORG-ID: ORG_ID
Content-Type: application/json
```

## Final Navigation Flow

1. Public user opens `/organization/register`.
2. Organization and first admin are created.
3. User logs in at `/login`.
4. Login response is stored in HTTP session.
5. Authenticated user lands on `/console/dashboard`.
6. Sidebar renders modules based on session permissions.
7. AJAX calls use `RbfApp.ajax`, attach JWT and org headers, and handle 401/403/500 consistently.

## Testing Checklist

- Login with a valid user and verify redirect to `/console/dashboard`.
- Confirm header shows username and organization display name.
- Open each visible sidebar item and confirm common layout is consistent.
- Verify hidden menu items for a low-permission user.
- Trigger an AJAX call and inspect headers for `Authorization` and `X-ORG-ID`.
- Expire or remove session and verify page requests redirect to `/login`.
- Expire or remove session and verify AJAX requests return 401 JSON.
- Test a forbidden action and verify a 403 toast.
- Stop a backend service and verify a controlled error toast.
- Confirm product, inventory, billing, reports, customer, supplier, purchase, and settings screens still load.
