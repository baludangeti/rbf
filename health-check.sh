#!/usr/bin/env sh

check() {
  service="$1"
  port="$2"
  body="$(curl -fsS --max-time 4 "http://localhost:$port/actuator/health" 2>/dev/null)"
  if [ $? -ne 0 ]; then
    echo "$service:$port NOT_REACHABLE"
    return
  fi
  echo "$body" | grep -q '"status":"UP"'
  if [ $? -eq 0 ]; then
    echo "$service:$port UP"
  else
    echo "$service:$port DOWN"
  fi
}

check api-gateway 8080
check auth-service 8081
check organization-service 8082
check product-service 8083
check inventory-service 8084
check billing-service 8085
check payment-service 8086
check accounting-service 8087
check report-service 8088
check tax-service 8089
check web-console 8090
