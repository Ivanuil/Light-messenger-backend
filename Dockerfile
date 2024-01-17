# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS final

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY src ./src

EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=5s --start-period=15s CMD curl --fail localhost:8080/actuator/health || exit 1

CMD ["./mvnw", "spring-boot:run", "-Djacoco.skip=true"]
