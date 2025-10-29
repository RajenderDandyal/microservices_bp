FROM alpine/java:21-jdk
WORKDIR /app
COPY target/invoice-service*.jar app.jar
EXPOSE 8002
ENV SPRING_PROFILES_ACTIVE=local
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
