FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app

COPY ./target/cryptomonnaie-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 8082

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "application.jar"]
