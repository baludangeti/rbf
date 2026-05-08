#!/usr/bin/env sh
set -eu

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
MVNW="$ROOT/../rbf-gateway/mvnw"
if [ ! -x "$MVNW" ]; then
  MVNW=mvn
fi
LOG_DIR="$ROOT/logs"
mkdir -p "$LOG_DIR"

echo "Building all required services..."
"$MVNW" -f "$ROOT/pom.xml" -pl api-gateway,auth-service,organization-service,product-service,inventory-service,billing-service,payment-service,accounting-service,report-service,tax-service,console-web -am clean install -DskipTests

start_service() {
  service="$1"
  artifact="$2"
  echo "Starting $service..."
  nohup java -jar "$ROOT/$service/target/$artifact" > "$LOG_DIR/$service.log" 2>&1 &
  sleep 5
}

start_service auth-service auth-service-0.0.1-SNAPSHOT.jar
start_service organization-service organization-service-0.0.1-SNAPSHOT.jar
start_service product-service product-service-0.0.1-SNAPSHOT.jar
start_service inventory-service inventory-service-0.0.1-SNAPSHOT.jar
start_service payment-service payment-service-0.0.1-SNAPSHOT.jar
start_service accounting-service accounting-service-0.0.1-SNAPSHOT.jar
start_service tax-service tax-service-0.0.1-SNAPSHOT.jar
start_service billing-service billing-service-0.0.1-SNAPSHOT.jar
start_service report-service report-service-0.0.1-SNAPSHOT.jar
start_service api-gateway api-gateway-0.0.1-SNAPSHOT.jar
start_service console-web console-web.war

echo "All services launched. Run ./health-check.sh after startup settles."
