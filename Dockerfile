FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app

COPY ./target/cryptomonnaie-0.0.1-SNAPSHOT.jar application.jar

# Copier le fichier Firebase et son contenu
COPY src/main/resources/firebase/ /app/firebase/

EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "application.jar"]
