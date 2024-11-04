FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/text-generation-service-0.0.1-SNAPSHOT.jar text-generation-service.jar
CMD ["java", "-jar", "text-generation-service.jar"]
