FROM alpine/java:21-jdk
WORKDIR /app
COPY target/user-service*.jar app.jar
EXPOSE 8001
ENV SPRING_PROFILES_ACTIVE=local
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]

