#!/bin/bash

# 변수 설정 부분
BUILD_JAR="bankassetor-0.0.1-SNAPSHOT.jar"
EC2_USER_HOST="ubuntu@52.78.49.240"   # Blue/Green 모두 동일 IP의 동일 EC2 인스턴스
SSH_KEY="~/dev/keys/bankKey.pem"
MAVEN_WRAPPER="./mvnw"

BLUE_PORT=8080
GREEN_PORT=8090

BLUE_PATH="/home/ubuntu/apps/bankly/lib/${BUILD_JAR}"
GREEN_PATH="/home/ubuntu/apps/bankly-green/lib/${BUILD_JAR}"

CADDY_API_ENDPOINT="http://localhost:2019/load"
HEALTH_ENDPOINT="/health-check"
API_DOMAIN="https://dev.bankly.store"

# Blue/Green 서버 디렉토리 별 restart 스크립트 경로
BLUE_RESTART_SCRIPT="/home/ubuntu/apps/bankly/bin/restart.sh"
GREEN_RESTART_SCRIPT="/home/ubuntu/apps/bankly-green/bin/restart.sh"

SSH_TUNNEL_PID=""

# Caddy 설정 JSON 템플릿 함수
# $1: upstream host:port
caddy_config_json() {
cat <<EOF
{
  "apps": {
    "http": {
      "servers": {
        "myserver": {
          "listen": [":443"],
          "routes": [
            {
              "match": [
                {
                  "host": ["bankly.store"]
                }
              ],
              "handle": [
                {
                  "handler": "reverse_proxy",
                  "upstreams": [
                    {
                      "dial": "$1"
                    }
                  ]
                }
              ]
            },
            {
              "match": [
                {
                  "host": ["dev.bankly.store"]
                }
              ],
              "handle": [
                {
                  "handler": "reverse_proxy",
                  "upstreams": [
                    {
                      "dial": "$1"
                    }
                  ]
                }
              ]
            },
            {
              "match": [
                {
                  "host": ["test.bankly.store"]
                }
              ],
              "handle": [
                {
                  "handler": "reverse_proxy",
                  "upstreams": [
                    {
                      "dial": "$1"
                    }
                  ]
                }
              ]
            }
          ]
        }
      }
    }
  }
}
EOF
}

# 헬스체크 함수
# $1: 서버 포트
check_health() {
  PORT=$1
  for i in {1..30}; do
    HTTP_STATUS=$(ssh -i $SSH_KEY $EC2_USER_HOST "curl -s -o /dev/null -w '%{http_code}' http://localhost:${PORT}${HEALTH_ENDPOINT}")
    if [ "$HTTP_STATUS" == "200" ]; then
      echo "Server on port $PORT is healthy"
      return 0
    fi
    sleep 2
  done
  echo "Health check failed for port $PORT"
  return 1
}

# 최종 API 도메인 체크 함수
check_final_api() {
  for i in {1..10}; do
    HTTP_STATUS=$(curl -s -o /dev/null -w '%{http_code}' ${API_DOMAIN}${HEALTH_ENDPOINT})
    if [ "$HTTP_STATUS" == "200" ]; then
      echo "API domain check success!"
      return 0
    fi
    sleep 2
  done
  echo "API domain check failed!"
  return 1
}

# SSH 포트포워딩 설정 함수
set_up_ssh_tunnel() {
  # 이미 터널이 있다면 중복 생성하지 않음
  if pgrep -f "ssh -i $SSH_KEY -L 2019:localhost:2019 $EC2_USER_HOST" > /dev/null; then
    echo "SSH tunnel already exists."
    return
  fi

  echo "Setting up SSH port forwarding..."
  ssh -i $SSH_KEY -L 2019:localhost:2019 $EC2_USER_HOST -N -f
  if [ $? -ne 0 ]; then
    echo "Failed to establish SSH tunnel."
    exit 1
  fi
  SSH_TUNNEL_PID=$(pgrep -f "ssh -i $SSH_KEY -L 2019:localhost:2019 $EC2_USER_HOST")
}

# SSH 포트포워딩 종료 함수
tear_down_ssh_tunnel() {
  if [ -n "$SSH_TUNNEL_PID" ]; then
    echo "Tearing down SSH tunnel..."
    kill $SSH_TUNNEL_PID
  fi
}

# 스크립트 종료 시 SSH 터널 정리
trap tear_down_ssh_tunnel EXIT

# 1. 로컬 빌드
#${MAVEN_WRAPPER} clean package -DskipTests
#if [ $? -ne 0 ]; then
#   echo "Build failed!"
#   exit 1
#fi

# 2. Green 서버에 새 버전 업로드 및 재시작
echo "Deploying to Green server..."
scp -i $SSH_KEY build/libs/${BUILD_JAR} ${EC2_USER_HOST}:${GREEN_PATH}
ssh -i $SSH_KEY ${EC2_USER_HOST} "cd /home/ubuntu/apps/bankly-green/bin && ./restart.sh"

# 3. Green 헬스체크
check_health ${GREEN_PORT} || exit 1

# SSH 포트포워딩 설정 (Caddy API 접근용)
set_up_ssh_tunnel

# 4. Caddy 설정을 Green 서버로 변경
echo "Switching Caddy upstream to Green..."
GREEN_CONFIG=$(caddy_config_json "localhost:${GREEN_PORT}")
curl -X POST -H "Content-Type: application/json" -d "${GREEN_CONFIG}" ${CADDY_API_ENDPOINT}
sleep 2

# 5. 최종 도메인 헬스 체크 (Green 활성화 확인)
check_final_api || exit 1

# 6. Blue 서버에 새 버전 업로드 및 재시작
echo "Deploying to Blue server..."
scp -i $SSH_KEY build/libs/${BUILD_JAR} ${EC2_USER_HOST}:${BLUE_PATH}
ssh -i $SSH_KEY ${EC2_USER_HOST} "cd /home/ubuntu/apps/bankly/bin && ./restart.sh"

# 7. Blue 헬스체크
check_health ${BLUE_PORT} || exit 1

# 8. Caddy 설정을 Blue 서버로 변경
echo "Switching Caddy upstream to Blue..."
BLUE_CONFIG=$(caddy_config_json "localhost:${BLUE_PORT}")
curl -X POST -H "Content-Type: application/json" -d "${BLUE_CONFIG}" ${CADDY_API_ENDPOINT}
sleep 2

# 9. 최종 도메인 헬스 체크 (Blue 활성화 확인)
check_final_api || exit 1

echo "Blue-Green deployment completed successfully, Blue is live."
exit 0
