# Sử dụng JDK 17 làm base image
FROM openjdk:17-jdk


# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR từ thư mục target vào container
COPY target/event-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8082 để ứng dụng có thể nhận request
EXPOSE 8082
# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
