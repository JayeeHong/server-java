# Java 17을 기반으로 한 이미지 사용
FROM openjdk:17-jdk-alpine

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
