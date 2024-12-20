# Étape 1 : Construire l'application
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Définir le répertoire de travail pour Maven
WORKDIR /app

# Copier uniquement le fichier pom.xml pour télécharger les dépendances
COPY pom.xml ./

# Télécharger les dépendances sans compiler le projet
RUN mvn dependency:go-offline

# Copier le code source du projet
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# Étape 2 : Créer l'image finale pour exécuter l'application
FROM eclipse-temurin:17-jdk

# Définir le répertoire de travail pour l'application
WORKDIR /app

# Copier le JAR généré depuis l'étape de build
COPY --from=build /app/target/utilisateur-0.0.1-SNAPSHOT.jar application.jar

# Exposer le port utilisé par l'application
EXPOSE 8082

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "application.jar"]
