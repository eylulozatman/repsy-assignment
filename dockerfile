# 1. Java 21 JDK içeren Temurin Alpine imajını kullan
FROM eclipse-temurin:21-jdk-alpine

# 2. Uygulamanın JAR dosyasını kopyala
COPY target/*.jar app.jar

# 3. JAR dosyasını çalıştır
ENTRYPOINT ["java", "-jar", "/app.jar"]
