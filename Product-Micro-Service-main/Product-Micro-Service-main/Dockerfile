FROM openjdk:21-jdk-slim

WORKDIR /app

# Maven wrapper ve pom.xml'i kopyala
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Maven wrapper'ı çalıştırılabilir yap
RUN chmod +x ./mvnw

# Dependency'leri indir (caching için)
RUN ./mvnw dependency:go-offline -B

# Source code'u kopyala
COPY src ./src

# Uygulamayı build et
RUN ./mvnw clean package -DskipTests

# Port'u expose et
EXPOSE 8080

# JAR dosyasını çalıştır (product-service artifactId'sine göre)
ENTRYPOINT ["java", "-jar", "target/product-service-0.0.1-SNAPSHOT.jar"]