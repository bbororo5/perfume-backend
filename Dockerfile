############# 이하 1단계 : 빌드
FROM gradle:8.7-jdk21 AS build

WORKDIR /home/gradle/project

COPY . .

RUN ./gradlew build --no-daemon

############# 이하 2단계 : 실행
FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]