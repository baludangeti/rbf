@echo off
setlocal enabledelayedexpansion

set ROOT=%~dp0
set MVNW=%ROOT%..\rbf-gateway\mvnw.cmd
if not exist "%MVNW%" set MVNW=mvn
set LOG_DIR=%ROOT%logs
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo Building all required services...
call "%MVNW%" -f "%ROOT%pom.xml" -pl api-gateway,auth-service,organization-service,product-service,inventory-service,billing-service,payment-service,accounting-service,report-service,tax-service,console-web -am clean install -DskipTests
if errorlevel 1 (
  echo Build failed. Check Maven output.
  exit /b 1
)

call :start_service auth-service auth-service-0.0.1-SNAPSHOT.jar
call :start_service organization-service organization-service-0.0.1-SNAPSHOT.jar
call :start_service product-service product-service-0.0.1-SNAPSHOT.jar
call :start_service inventory-service inventory-service-0.0.1-SNAPSHOT.jar
call :start_service payment-service payment-service-0.0.1-SNAPSHOT.jar
call :start_service accounting-service accounting-service-0.0.1-SNAPSHOT.jar
call :start_service tax-service tax-service-0.0.1-SNAPSHOT.jar
call :start_service billing-service billing-service-0.0.1-SNAPSHOT.jar
call :start_service report-service report-service-0.0.1-SNAPSHOT.jar
call :start_service api-gateway api-gateway-0.0.1-SNAPSHOT.jar
call :start_service console-web console-web.war

echo All services launched. Run health-check.bat after startup settles.
exit /b 0

:start_service
set SERVICE=%~1
set ARTIFACT=%~2
echo Starting %SERVICE%...
start "%SERVICE%" cmd /c "java -jar ""%ROOT%%SERVICE%\target\%ARTIFACT%"" > ""%LOG_DIR%\%SERVICE%.log"" 2>&1"
timeout /t 5 /nobreak > nul
exit /b 0
