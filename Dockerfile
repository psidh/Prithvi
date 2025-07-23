FROM eclipse-temurin:22-jre-jammy

WORKDIR /app

COPY prithvi.jar .

EXPOSE 1902

CMD ["java", "-jar", "prithvi.jar"]
