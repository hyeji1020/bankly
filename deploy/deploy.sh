# 프로젝트 디렉터리로 이동
cd /c/code/bankassetor

# 프로젝트 빌드 (테스트는 실행하지 않음)
./gradlew build -x test
echo "테스트 제외한 프로젝트 빌드"

# 빌드된 JAR 파일을 서버로 복사
scp -i /c/code/bankKey.pem build/libs/bankassetor-0.0.1-SNAPSHOT.jar ubuntu@52.78.49.240:/home/ubuntu/apps/bankly/lib
echo "빌드된 jar 파일 서버로 복사 완료"

# 서버에 접속하여 애플리케이션 재시작
ssh -i /c/code/bankKey.pem ubuntu@52.78.49.240 "cd /home/ubuntu/apps/bankly/bin && bash restart.sh"
echo "서버 접속하여 애플리케이션 재시작 완료"

# 서버에 접속하여 로그 확인
ssh -i /c/code/bankKey.pem ubuntu@52.78.49.240 "cd /home/ubuntu/apps/bankly/bin && docker compose logs -f"
