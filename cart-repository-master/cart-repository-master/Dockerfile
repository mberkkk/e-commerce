FROM eclipse-temurin:21-jdk-alpine

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
EXPOSE 8082

# JAR dosyasını çalıştır (cart-service artifactId'sine göre)
ENTRYPOINT ["java", "-jar", "target/cart-service-0.0.1-SNAPSHOT.jar"]