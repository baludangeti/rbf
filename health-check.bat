@echo off
setlocal

call :check api-gateway 8080
call :check auth-service 8081
call :check organization-service 8082
call :check product-service 8083
call :check inventory-service 8084
call :check billing-service 8085
call :check payment-service 8086
call :check accounting-service 8087
call :check report-service 8088
call :check tax-service 8089
call :check web-console 8090
exit /b 0

:check
set SERVICE=%~1
set PORT=%~2
for /f "usebackq delims=" %%S in (`powershell -NoProfile -Command "try { $r = Invoke-RestMethod -Uri 'http://localhost:%PORT%/actuator/health' -TimeoutSec 4; if ($r.status -eq 'UP') { 'UP' } else { 'DOWN' } } catch { 'NOT_REACHABLE' }"`) do set STATUS=%%S
echo %SERVICE%:%PORT% %STATUS%
exit /b 0
