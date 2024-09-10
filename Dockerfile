FROM eclipse-temurin:17 as build
LABEL authors="aquashdw"

WORKDIR /app
COPY . .

RUN <<EOF
./gradlew bootJar
mv build/libs/*.jar app.jar
EOF

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY application.yaml .
COPY --from=build /app/app.jar .

CMD ["java", "-jar", "-Dspring.config.location=file:/app/application.yaml", "app.jar"]
EXPOSE 8080

