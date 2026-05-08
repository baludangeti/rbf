# Frontend Console - Spring MVC + JSP Structure

Module: `console-web`

Tech stack:

- Spring Boot
- Spring MVC
- JSP
- JSTL
- Bootstrap 5
- jQuery/AJAX
- JavaScript
- HTTP Session

Not used:

- Thymeleaf
- React
- Angular

## Folder Structure

```text
console-web/
  pom.xml
  src/main/java/com/rbf/product/console/
    ConsoleWebApplication.java
    config/
      MvcViewConfig.java
    controller/
      AuthConsoleController.java
      PublicRegistrationController.java
      AdminConsoleController.java
      PosConsoleController.java
      GatewayAjaxProxyController.java
    dto/
      LoginForm.java
      OrganizationRegistrationForm.java
  src/main/resources/
    application.yml
  src/main/webapp/
    WEB-INF/views/
      layouts/
        header.jsp
        sidebar.jsp
        footer.jsp
        scripts.jsp
      auth/
        login.jsp
        forgot-password.jsp
        session-expired.jsp
      organization/
        register.jsp
        register-admin.jsp
        success.jsp
      console/
        dashboard.jsp
        users.jsp
        roles.jsp
        permissions.jsp
        settings.jsp
      billing/
        pos.jsp
        invoice-view.jsp
        sales-return.jsp
      product/
        products.jsp
        product-form.jsp
        categories.jsp
      inventory/
        stock.jsp
        stock-adjustment.jsp
      reports/
        sales-report.jsp
        gst-report.jsp
        inventory-report.jsp
    static/
      css/console.css
      js/console.js
```

## MVC Routes

Public registration:

- `GET /organization/register`
- `POST /organization/register`
- `POST /organization/register-admin`

Login/session:

- `GET /auth/login`
- `POST /auth/login`
- `GET /auth/forgot-password`
- `GET /auth/session-expired`
- `POST /auth/logout`

Admin console:

- `GET /console/dashboard`
- `GET /console/users`
- `GET /console/roles`
- `GET /console/permissions`
- `GET /console/settings`

Billing console:

- `GET /billing/pos`
- `GET /billing/invoice-view`
- `GET /billing/sales-return`

Product console:

- `GET /product/products`
- `GET /product/product-form`
- `GET /product/categories`

Inventory console:

- `GET /inventory/stock`
- `GET /inventory/stock-adjustment`

Reports console:

- `GET /reports/sales-report`
- `GET /reports/gst-report`
- `GET /reports/inventory-report`

AJAX proxy:

- `/gateway/**`

The AJAX proxy forwards browser calls to the API Gateway configured by:

```yaml
services:
  gateway:
    base-url: http://localhost:9100
```

It forwards:

- `Authorization`
- `X-ORG-ID`
- request body
- query string

## View Resolver

Configured in `application.yml`:

```yaml
spring:
  mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp
```

## Run

```bash
mvn -pl console-web -am -DskipTests package
java -jar console-web/target/console-web.war
```

Default URL:

```text
http://localhost:9120/public/register
http://localhost:9120/organization/register
```
