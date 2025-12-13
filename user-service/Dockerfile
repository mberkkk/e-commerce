
# 1. Build aşaması
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Proje dosyalarını kopyala
COPY . .

# Maven ile jar dosyasını oluştur
RUN mvn clean package -DskipTests

# 2. Run aşaması
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Build aşamasından jar dosyasını kopyala
COPY --from=build /app/target/*.jar app.jar

# Portu expose et
EXPOSE 8083

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]