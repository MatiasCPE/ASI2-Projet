FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/request-management-service-0.0.1-SNAPSHOT.jar request-management-service.jar
CMD ["java", "-jar", "request-management-service.jar"]
