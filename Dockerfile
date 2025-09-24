FROM alpine/java:21-jdk
WORKDIR /app
COPY user-service/target/user-service*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
