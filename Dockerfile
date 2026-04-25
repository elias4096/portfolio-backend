# docker build -t bryrmiginte/portfolio-backend:latest .
# docker push bryrmiginte/portfolio-backend:latest

# docker run -p 8080:8080 bryrmiginte/portfolio-backend:latest

FROM eclipse-temurin:26 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:26-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
